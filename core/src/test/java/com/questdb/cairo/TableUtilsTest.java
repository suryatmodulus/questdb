package com.questdb.cairo;

import com.questdb.misc.Files;
import com.questdb.misc.FilesFacade;
import com.questdb.misc.FilesFacadeImpl;
import com.questdb.std.str.LPSZ;
import com.questdb.std.str.Path;
import com.questdb.store.ColumnType;
import com.questdb.store.PartitionBy;
import com.questdb.store.factory.configuration.JournalMetadata;
import com.questdb.store.factory.configuration.JournalStructure;
import com.questdb.test.tools.TestUtils;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static com.questdb.cairo.TableUtils.TABLE_EXISTS;
import static com.questdb.cairo.TableUtils.TABLE_RESERVED;

public class TableUtilsTest {
    private final static FilesFacade FF = FilesFacadeImpl.INSTANCE;
    @ClassRule
    public static TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testCreate() throws Exception {

        final CharSequence root = temp.getRoot().getAbsolutePath();
        final JournalMetadata metadata = getJournalStructure().build();

        try (AppendMemory appendMemory = new AppendMemory()) {
            try (Path path = new Path()) {
                TableUtils.create(FF, path, appendMemory, root, metadata, 509);
                Assert.assertEquals(TABLE_EXISTS, TableUtils.exists(FF, path, root, metadata.getName()));

                path.of(root).concat(metadata.getName()).concat("_meta").$();

                try (ReadOnlyMemory mem = new ReadOnlyMemory(FF, path, Files.PAGE_SIZE)) {
                    long p = 0;
                    Assert.assertEquals(metadata.getColumnCount(), mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(PartitionBy.NONE, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(metadata.getTimestampIndex(), mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.INT, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.DOUBLE, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.FLOAT, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.BYTE, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.LONG, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.STRING, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.BOOLEAN, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.SYMBOL, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.SHORT, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.DATE, mem.getInt(p));
                    p += 4;
                    Assert.assertEquals(ColumnType.DATE, mem.getInt(p));
                    p += 4;

                    p = assertCol(mem, p, "i");
                    p = assertCol(mem, p, "d");

                    p = assertCol(mem, p, "f");
                    p = assertCol(mem, p, "b");
                    p = assertCol(mem, p, "l");
                    p = assertCol(mem, p, "str");
                    p = assertCol(mem, p, "boo");
                    p = assertCol(mem, p, "sym");
                    p = assertCol(mem, p, "sho");
                    p = assertCol(mem, p, "date");
                    assertCol(mem, p, "timestamp");
                }
            }
        }
    }

    @Test
    public void testCreateFailure() throws Exception {
        TestUtils.assertMemoryLeak(() -> {
            class X extends FilesFacadeImpl {
                @Override
                public int mkdirs(LPSZ path, int mode) {
                    return -1;
                }
            }

            X ff = new X();
            JournalMetadata metadata = getJournalStructure().build();
            try (AppendMemory appendMemory = new AppendMemory()) {
                try (Path path = new Path()) {
                    TableUtils.create(ff, path, appendMemory, temp.getRoot().getAbsolutePath(), metadata, 509);
                    Assert.fail();
                } catch (CairoException e) {
                    Assert.assertNotNull(e.getMessage());
                }
            }
        });
    }

    @Test
    public void testForeignDirectory() throws Exception {
        try (Path path = new Path()) {
            Assert.assertEquals(TABLE_RESERVED, TableUtils.exists(FF, path, temp.getRoot().getAbsolutePath(), ""));
        }
    }

    @Test
    public void testInstantiation() throws Exception {
        new TableUtils();
    }

    @Test
    public void testUnknownTodo() throws Exception {
        TestUtils.assertEquals("unknown", TableUtils.getTodoText(7879797987L));
    }

    private static long assertCol(ReadOnlyMemory mem, long p, CharSequence expected) {
        CharSequence name = mem.getStr(p);
        TestUtils.assertEquals(expected, name);
        Assert.assertNotNull(name);
        p += 4 + name.length() * 2;
        return p;
    }

    private JournalStructure getJournalStructure() {
        return new JournalStructure("abc")
                .$int("i")
                .$double("d")
                .$float("f")
                .$byte("b")
                .$long("l")
                .$str("str")
                .$bool("boo")
                .$sym("sym")
                .$short("sho")
                .$date("date")
                .$ts()
                .$();
    }
}