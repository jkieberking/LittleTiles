package com.creativemd.littletiles.common.utils.collision;

import org.joml.Matrix3d;
import org.joml.Matrix4d;
import scala.swing.model.Matrix;

public class MatrixUtilsProxy {

    public static Matrix3d createIdentityMatrix() {
        Matrix3d matrix = new Matrix3d();
        matrix.identity();
        return matrix;
    }

    public static Matrix3d createRotationMatrix(double rotX, double rotY, double rotZ) {
        Matrix3d matrix = createRotationMatrixX(rotX);
        matrix.mul(createRotationMatrixY(rotY));
        matrix.mul(createRotationMatrixZ(rotZ));
        return matrix;
    }

    public static void mul(Matrix4d matrix, Matrix3d matrix2) {
        double m00, m01, m02, m10, m11, m12, m20, m21, m22;

        m00 = matrix.m00() * matrix2.m00 + matrix.m01() * matrix2.m10 + matrix.m02() * matrix2.m20;
        m01 = matrix.m00() * matrix2.m01 + matrix.m01() * matrix2.m11 + matrix.m02() * matrix2.m21;
        m02 = matrix.m00() * matrix2.m02 + matrix.m01() * matrix2.m12 + matrix.m02() * matrix2.m22;

        m10 = matrix.m10() * matrix2.m00 + matrix.m11() * matrix2.m10 + matrix.m12() * matrix2.m20;
        m11 = matrix.m10() * matrix2.m01 + matrix.m11() * matrix2.m11 + matrix.m12() * matrix2.m21;
        m12 = matrix.m10() * matrix2.m02 + matrix.m11() * matrix2.m12 + matrix.m12() * matrix2.m22;

        m20 = matrix.m20() * matrix2.m00 + matrix.m21() * matrix2.m10 + matrix.m22() * matrix2.m20;
        m21 = matrix.m20() * matrix2.m01 + matrix.m21() * matrix2.m11 + matrix.m22() * matrix2.m21;
        m22 = matrix.m20() * matrix2.m02 + matrix.m21() * matrix2.m12 + matrix.m22() * matrix2.m22;

        matrix.m00(m00);
        matrix.m01(m01);
        matrix.m02(m02);
        matrix.m10(m10);
        matrix.m11(m11);
        matrix.m12(m12);
        matrix.m20(m20);
        matrix.m21(m21);
        matrix.m22(m22);
    }

    public static Matrix4d createRotationMatrixAndTranslationRadians(double x, double y, double z, double rotX, double rotY, double rotZ) {
        Matrix4d matrix = new Matrix4d();
        if (rotX != 0)
            matrix.rotateX(rotX);
        else
            matrix.identity();

        if (rotY != 0)
            mul(matrix, createRotationMatrixYRadians(rotY));
        if (rotZ != 0)
            mul(matrix, createRotationMatrixZRadians(rotZ));

        matrix.m03(x);
        matrix.m13(y);
        matrix.m23(z);

        return matrix;
    }

    public static Matrix4d createRotationMatrixAndTranslation(double x, double y, double z, double rotX, double rotY, double rotZ) {
        Matrix4d matrix = new Matrix4d();
        if (rotX != 0)
            matrix.rotateX(Math.toRadians(rotX));
        else
            matrix.identity();

        if (rotY != 0)
            mul(matrix, createRotationMatrixY(rotY));
        if (rotZ != 0)
            mul(matrix, createRotationMatrixZ(rotZ));

        matrix.m03(x);
        matrix.m13(y);
        matrix.m23(z);

        return matrix;
    }

    public static Matrix3d createRotationMatrixX(double angle) {
        Matrix3d matrix = new Matrix3d();
        matrix.rotateX(Math.toRadians(angle));
        return matrix;
    }

    private static Matrix3d createRotationMatrixXRadians(double radians) {
        Matrix3d matrix = new Matrix3d();
        matrix.rotateX(radians);
        return matrix;
    }

    public static Matrix3d createRotationMatrixY(double angle) {
        Matrix3d matrix = new Matrix3d();
        matrix.rotateY(Math.toRadians(angle));
        return matrix;
    }

    private static Matrix3d createRotationMatrixYRadians(double radians) {
        Matrix3d matrix = new Matrix3d();
        matrix.rotateY(radians);
        return matrix;
    }

    public static Matrix3d createRotationMatrixZ(double angle) {
        Matrix3d matrix = new Matrix3d();
        matrix.rotateZ(Math.toRadians(angle));
        return matrix;
    }

    private static Matrix3d createRotationMatrixZRadians(double radians) {
        Matrix3d matrix = new Matrix3d();
        matrix.rotateZ(radians);
        return matrix;
    }

}
