package org.dynmap.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class Matrix3DTest {

    private static final double DELTA = 1e-10;

    @Test
    public void testIdentityConstructor() {
        Matrix3D m = new Matrix3D();
        double[] v = {1.0, 2.0, 3.0};
        m.transform(v);
        // Identity matrix should not change the vector
        assertEquals(1.0, v[0], DELTA);
        assertEquals(2.0, v[1], DELTA);
        assertEquals(3.0, v[2], DELTA);
    }

    @Test
    public void testParameterizedConstructor() {
        // Create a matrix that doubles each component
        Matrix3D m = new Matrix3D(2, 0, 0, 0, 2, 0, 0, 0, 2);
        double[] v = {1.0, 2.0, 3.0};
        m.transform(v);
        assertEquals(2.0, v[0], DELTA);
        assertEquals(4.0, v[1], DELTA);
        assertEquals(6.0, v[2], DELTA);
    }

    @Test
    public void testTransformArray() {
        Matrix3D m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9);
        double[] v = {1.0, 1.0, 1.0};
        m.transform(v);
        // Row 1: 1*1 + 2*1 + 3*1 = 6
        // Row 2: 4*1 + 5*1 + 6*1 = 15
        // Row 3: 7*1 + 8*1 + 9*1 = 24
        assertEquals(6.0, v[0], DELTA);
        assertEquals(15.0, v[1], DELTA);
        assertEquals(24.0, v[2], DELTA);
    }

    @Test
    public void testTransformVector3D() {
        Matrix3D m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Vector3D v = new Vector3D(1.0, 1.0, 1.0);
        m.transform(v);
        assertEquals(6.0, v.x, DELTA);
        assertEquals(15.0, v.y, DELTA);
        assertEquals(24.0, v.z, DELTA);
    }

    @Test
    public void testTransformVector3DWithOutput() {
        Matrix3D m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Vector3D v = new Vector3D(1.0, 1.0, 1.0);
        Vector3D out = new Vector3D();
        m.transform(v, out);
        // Input should be unchanged
        assertEquals(1.0, v.x, DELTA);
        assertEquals(1.0, v.y, DELTA);
        assertEquals(1.0, v.z, DELTA);
        // Output should have transformed values
        assertEquals(6.0, out.x, DELTA);
        assertEquals(15.0, out.y, DELTA);
        assertEquals(24.0, out.z, DELTA);
    }

    @Test
    public void testScale() {
        Matrix3D m = new Matrix3D();
        m.scale(2.0, 3.0, 4.0);
        double[] v = {1.0, 1.0, 1.0};
        m.transform(v);
        assertEquals(2.0, v[0], DELTA);
        assertEquals(3.0, v[1], DELTA);
        assertEquals(4.0, v[2], DELTA);
    }

    @Test
    public void testRotateXY90Degrees() {
        Matrix3D m = new Matrix3D();
        m.rotateXY(90.0);
        double[] v = {1.0, 0.0, 0.0};
        m.transform(v);
        // X axis rotated 90 degrees clockwise around Z should point to Y
        assertEquals(0.0, v[0], DELTA);
        assertEquals(-1.0, v[1], DELTA);
        assertEquals(0.0, v[2], DELTA);
    }

    @Test
    public void testRotateXZ90Degrees() {
        Matrix3D m = new Matrix3D();
        m.rotateXZ(90.0);
        double[] v = {1.0, 0.0, 0.0};
        m.transform(v);
        // X axis rotated 90 degrees clockwise around Y should point to +Z
        assertEquals(0.0, v[0], DELTA);
        assertEquals(0.0, v[1], DELTA);
        assertEquals(1.0, v[2], DELTA);
    }

    @Test
    public void testRotateYZ90Degrees() {
        Matrix3D m = new Matrix3D();
        m.rotateYZ(90.0);
        double[] v = {0.0, 1.0, 0.0};
        m.transform(v);
        // Y axis rotated 90 degrees clockwise around X should point to Z
        assertEquals(0.0, v[0], DELTA);
        assertEquals(0.0, v[1], DELTA);
        assertEquals(-1.0, v[2], DELTA);
    }

    @Test
    public void testRotate360DegreesReturnsToOriginal() {
        Matrix3D m = new Matrix3D();
        m.rotateXY(360.0);
        double[] v = {1.0, 2.0, 3.0};
        m.transform(v);
        assertEquals(1.0, v[0], DELTA);
        assertEquals(2.0, v[1], DELTA);
        assertEquals(3.0, v[2], DELTA);
    }

    @Test
    public void testMultiply() {
        // Create two scaling matrices
        Matrix3D m1 = new Matrix3D(2, 0, 0, 0, 2, 0, 0, 0, 2);
        Matrix3D m2 = new Matrix3D(3, 0, 0, 0, 3, 0, 0, 0, 3);
        m1.multiply(m2);
        double[] v = {1.0, 1.0, 1.0};
        m1.transform(v);
        // Should scale by 6 (2*3)
        assertEquals(6.0, v[0], DELTA);
        assertEquals(6.0, v[1], DELTA);
        assertEquals(6.0, v[2], DELTA);
    }

    @Test
    public void testShearZ() {
        Matrix3D m = new Matrix3D();
        m.shearZ(1.0, 0.0);
        // With shearZ matrix [1,0,0; 0,1,0; x_fact,y_fact,1] multiplied as mat*this
        // The result matrix has x_fact in position m31
        // transform: v1 = m11*x + m12*y + m13*z = x
        //            v2 = m21*x + m22*y + m23*z = y
        //            v3 = m31*x + m32*y + m33*z = x_fact*x + y_fact*y + z
        double[] v = {1.0, 0.0, 0.0};
        m.transform(v);
        // X contributes to Z via shear
        assertEquals(1.0, v[0], DELTA);
        assertEquals(0.0, v[1], DELTA);
        assertEquals(1.0, v[2], DELTA);
    }

    @Test
    public void testChainedTransformations() {
        Matrix3D m = new Matrix3D();
        m.scale(2.0, 2.0, 2.0);
        m.rotateXY(90.0);
        double[] v = {1.0, 0.0, 0.0};
        m.transform(v);
        // First scale by 2, then rotate - result should be scaled and rotated
        assertEquals(0.0, v[0], DELTA);
        assertEquals(-2.0, v[1], DELTA);
        assertEquals(0.0, v[2], DELTA);
    }

    @Test
    public void testToString() {
        Matrix3D m = new Matrix3D();
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
    }

    @Test
    public void testToJSON() {
        Matrix3D m = new Matrix3D(1, 2, 3, 4, 5, 6, 7, 8, 9);
        org.json.simple.JSONArray json = m.toJSON();
        assertEquals(9, json.size());
        assertEquals(1.0, json.get(0));
        assertEquals(2.0, json.get(1));
        assertEquals(3.0, json.get(2));
        assertEquals(4.0, json.get(3));
        assertEquals(5.0, json.get(4));
        assertEquals(6.0, json.get(5));
        assertEquals(7.0, json.get(6));
        assertEquals(8.0, json.get(7));
        assertEquals(9.0, json.get(8));
    }
}
