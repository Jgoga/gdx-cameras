package com.desenvolvedorindie.gdxcamera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

public class CameraZoom implements ICameraStrategy {

    private Interpolation interpolation;
    private float[] zoomLevels;
    private int zoomLevel = 1;
    private float duration;
    private float time;
    private boolean interporlate;

    public CameraZoom(float[] zoomLevels) {
        this(zoomLevels, 0, null);
    }

    public CameraZoom(float[] zoomLevels, float duration) {
        this(zoomLevels, duration, Interpolation.smooth);
    }

    public CameraZoom(float[] zoomLevels, float duration, Interpolation interpolation) {
        this.zoomLevels = zoomLevels;
        this.duration = duration;
        this.interpolation = interpolation;
    }

    public void zoomOut() {
        zoomLevel = ++zoomLevel % zoomLevels.length;
        startInterpolate();
    }

    public void zoomIn() {
        --zoomLevel;
        if (zoomLevel < 0) {
            zoomLevel = zoomLevels.length - 1;
        }
        startInterpolate();
    }

    private void startInterpolate() {
        time = 0;
        interporlate = true;
    }

    @Override
    public void update(Camera camera, float delta) {
        if (!(camera instanceof OrthographicCamera))
            throw new RuntimeException("CameraZoom only supports OrthographicCamera");

        if (interporlate) {
            OrthographicCamera cam = (OrthographicCamera) camera;

            if (interpolation != null) {
                time += delta;

                float progress = Math.min(1f, time / duration);

                if (progress == 1f) {
                    interporlate = false;
                }

                cam.zoom = interpolation.apply(cam.zoom, zoomLevels[zoomLevel], progress);

                cam.update();
            } else {
                cam.zoom = zoomLevels[zoomLevel];
                interporlate = false;
            }
        }
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public float[] getZoomLevels() {
        return zoomLevels;
    }

    public void setZoomLevels(float[] zoomLevels) {
        this.zoomLevels = zoomLevels;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
