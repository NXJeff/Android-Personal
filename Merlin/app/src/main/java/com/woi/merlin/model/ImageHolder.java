package com.woi.merlin.model;

import merlin.model.raw.BaseImageHolder;

/**
 * Created by YeekFeiTan on 3/4/2015.
 */
public class ImageHolder extends BaseImageHolder {

    public ImageHolder() {
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImageHolder other = (ImageHolder) obj;

        if (this.getId() != null && other.getId() != null) {
            return this.getId().equals(other.getId());
        }

        if (this.getEntityId() != null && other.getEntityId() != null) {
            return this.getEntityId().equals(other.getEntityId());
        }

        if (this.getPath() != null && other.getPath() != null) {
            return this.getPath().equals(other.getPath());
        }
        return true;
    }

}
