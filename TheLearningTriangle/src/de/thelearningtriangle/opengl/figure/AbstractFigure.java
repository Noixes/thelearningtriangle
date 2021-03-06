package de.thelearningtriangle.opengl.figure;

import java.awt.Color;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public abstract class AbstractFigure implements DrawableFigure {
	private float[][] vectors;
	private int rotationAngle;

	private int glMatrixFuncMode;
	private int gl2es3Mode;

	protected float posX;
	protected float posY;

	protected Color color;

	protected AbstractFigure(int glMatrixFuncMode, int gl2es3Mode, Color color) {
		this.glMatrixFuncMode = glMatrixFuncMode;
		this.gl2es3Mode = gl2es3Mode;
		this.vectors = new float[0][0];
		this.posX = 0f;
		this.posY = 0f;
		this.color = color;
	}

	/**
	 * Sets vectors of the figure
	 * 
	 * @param vectors
	 */
	protected void setFigureVectors(float[][] vectors) {
		this.vectors = vectors;
	}

	/**
	 * This function adds the angle to the overall rotation of this object
	 * 
	 * @param angle
	 */
	protected void addRotationFor(int angle) {
		this.rotationAngle = (this.rotationAngle + angle) % 360;
	}

	/**
	 * This function returns a value between 0 and 359 This value represents the
	 * rotation of this figure
	 * 
	 * @return
	 */
	protected int getRotationAngle() {
		return rotationAngle;
	}

	@Override
	public final void drawFigureWith(GLAutoDrawable drawable) {
		float surfaceWidth = drawable.getSurfaceWidth();
		float surfaceHeight = drawable.getSurfaceHeight();
		float ratio = surfaceWidth / surfaceHeight;

		float[][] rotatedVectors = calculateRotatedObject();

		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());

		gl.glMatrixMode(glMatrixFuncMode);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glBegin(gl2es3Mode);
		for (float[] vector : rotatedVectors) {
			gl.glVertex3f((vector[0] + posX) / ratio, vector[1] + posY, vector[2]);
		}
		gl.glEnd();
		gl.glPopMatrix();
	}

	/**
	 * This function calculates the rotation of the object
	 * 
	 * @param rotation
	 */
	private float[][] calculateRotatedObject() {
		float[][] rotatedVectors = new float[this.vectors.length][];

		for (int i = 0; i < rotatedVectors.length; i++) {
			float[] newVector = new float[3];

			float[] vector = this.vectors[i];
			newVector[0] = (float) (vector[0] * Math.cos(Math.toRadians(getRotationAngle())))
					- (float) (vector[1] * Math.sin(Math.toRadians(getRotationAngle())));
			newVector[1] = (float) (vector[0] * Math.sin(Math.toRadians(getRotationAngle())))
					+ (float) (vector[1] * Math.cos(Math.toRadians(getRotationAngle())));
			newVector[2] = 0.0f;

			rotatedVectors[i] = newVector;
		}

		return rotatedVectors;
	}

	protected abstract float[][] getBaseVectors();
}