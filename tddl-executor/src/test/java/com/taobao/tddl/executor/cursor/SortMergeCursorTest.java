package com.taobao.tddl.executor.cursor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.taobao.tddl.common.exception.TddlException;
import com.taobao.tddl.executor.cursor.impl.SortMergeJoinCursor;
import com.taobao.tddl.executor.rowset.IRowSet;
import com.taobao.tddl.optimizer.core.expression.ISelectable.DATA_TYPE;
import com.taobao.tddl.optimizer.core.expression.bean.Column;

public class SortMergeCursorTest {

    Cursor getCursor(String tableName, Integer[] ids) throws TddlException {
        MockArrayCursor cursor = new MockArrayCursor(tableName);
        cursor.addColumn("id", DATA_TYPE.INT_VAL);
        cursor.addColumn("name", DATA_TYPE.STRING_VAL);
        cursor.addColumn("school", DATA_TYPE.STRING_VAL);
        cursor.initMeta();

        for (Integer id : ids) {
            cursor.addRow(new Object[] { id, "name" + id, "school" + id });

        }

        cursor.init();

        return cursor;

    }

    @Test
    public void testInnerJoin() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 1, 1, 1, 2, 3, 4, 5, 6,
                7 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2",
            new Integer[] { 1, 1, 2, 2, 4, 5, 6, 7 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        Object[] expected = new Object[] { 1, 1, 1, 1, 1, 1, 2, 2, 4, 5, 6, 7 };
        List actual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            actual.add(row.getObject(0));
            System.out.println(row);
        }

        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testInnerJoinWithRightEmpty() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 1, 1, 1, 2, 3, 4, 5, 6,
                7 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] {}));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        Object[] expected = new Object[] {};
        List actual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            actual.add(row.getObject(0));
            System.out.println(row);
        }

        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testInnerJoinWithBothEmpty() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] {}));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] {}));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        Object[] expected = new Object[] {};
        List actual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            actual.add(row.getObject(0));
            System.out.println(row);
        }

        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testInnerJoinWithTwoJoinOnColumns() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 1, 1, 1, 2, 3, 4, 5, 6,
                7 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2",
            new Integer[] { 1, 1, 2, 2, 4, 5, 6, 7 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));
        leftJoinOnColumns.add(new Column().setColumnName("NAME").setDataType(DATA_TYPE.STRING_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));
        rightJoinOnColumns.add(new Column().setColumnName("NAME").setDataType(DATA_TYPE.STRING_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        Object[] expected = new Object[] { 1, 1, 1, 1, 1, 1, 2, 2, 4, 5, 6, 7 };
        List actual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            actual.add(row.getObject(0));
            System.out.println(row);
        }

        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testLeftJoin() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 3, 4, 5 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setLeftJoin(true);

        Object[] leftExpected = new Object[] { 2, 3, 4, 5 };
        Object[] rightExpected = new Object[] { 2, null, 4, 5 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testLeftJoin2() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 4, 5 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 3, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setLeftJoin(true);

        Object[] leftExpected = new Object[] { 2, 4, 5 };
        Object[] rightExpected = new Object[] { 2, 4, 5 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testRightJoin() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 3, 4, 5 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);

        Object[] leftExpected = new Object[] { 2, 4, 5 };
        Object[] rightExpected = new Object[] { 2, 4, 5 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testRightJoin2() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 4, 5 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 3, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);

        Object[] leftExpected = new Object[] { 2, null, 4, 5 };
        Object[] rightExpected = new Object[] { 2, 3, 4, 5 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testFullOutterJoin() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 3, 4, 5 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);
        c.setLeftJoin(true);

        Object[] leftExpected = new Object[] { 2, 3, 4, 5 };
        Object[] rightExpected = new Object[] { 2, null, 4, 5 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testFullOutterJoin2() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 4, 5 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 3, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);
        c.setLeftJoin(true);
        Object[] leftExpected = new Object[] { 2, null, 4, 5 };
        Object[] rightExpected = new Object[] { 2, 3, 4, 5 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testFullOutterJoin3() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 4, 5, 6 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 3, 4, 5 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);
        c.setLeftJoin(true);
        Object[] leftExpected = new Object[] { 2, null, 4, 5, 6 };
        Object[] rightExpected = new Object[] { 2, 3, 4, 5, null };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testFullOutterJoin4() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 2, 4, 5, 6 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] { 2, 3, 4, 5, 9 }));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);
        c.setLeftJoin(true);
        Object[] leftExpected = new Object[] { 2, null, 4, 5, 6, null };
        Object[] rightExpected = new Object[] { 2, 3, 4, 5, null, 9 };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }

    @Test
    public void testFullOutterJoin5() throws TddlException {

        ISchematicCursor left_cursor = new SchematicCursor(this.getCursor("T1", new Integer[] { 1, 1, 1, 2, 4, 5, 6, 6,
                6 }));
        ISchematicCursor right_cursor = new SchematicCursor(this.getCursor("T2", new Integer[] {}));

        List leftJoinOnColumns = new ArrayList();

        leftJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T1"));

        List rightJoinOnColumns = new ArrayList();

        rightJoinOnColumns.add(new Column().setColumnName("ID").setDataType(DATA_TYPE.INT_VAL).setTableName("T2"));

        SortMergeJoinCursor c = new SortMergeJoinCursor(left_cursor,
            right_cursor,
            leftJoinOnColumns,
            rightJoinOnColumns);

        c.setRightJoin(true);
        c.setLeftJoin(true);
        Object[] leftExpected = new Object[] { 1, 1, 1, 2, 4, 5, 6, 6, 6 };
        Object[] rightExpected = new Object[] { null, null, null, null, null, null, null, null, null };
        List leftActual = new ArrayList();
        List rightActual = new ArrayList();
        IRowSet row = null;
        while ((row = c.next()) != null) {
            leftActual.add(row.getObject(0));
            rightActual.add(row.getObject(3));
            System.out.println(row);
        }

        Assert.assertArrayEquals(leftExpected, leftActual.toArray());
        Assert.assertArrayEquals(rightExpected, rightActual.toArray());
    }
}