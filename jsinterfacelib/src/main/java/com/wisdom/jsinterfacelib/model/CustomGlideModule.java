package com.wisdom.jsinterfacelib.model;

import com.bumptech.glide.annotation.Excludes;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author HanXueFeng
 * @ProjectName project： jssdk_basic
 * @class package：com.wisdom.jsinterfacelib.model
 * @class describe：
 * @time 2021/7/16 0016 11:43
 * @change
 */
//@Excludes(value = OkHttpLibraryGlideModule.class)
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
