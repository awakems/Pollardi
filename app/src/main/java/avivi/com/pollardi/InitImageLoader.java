package avivi.com.pollardi;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Void on 06.05.2015.
 */
public class InitImageLoader extends Application {
    public void onCreate() {


        super.onCreate();
        Context context = getApplicationContext();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MIN_PRIORITY + 3);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(300 * 1024 * 1024); // 300 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.threadPoolSize(2);
//        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }
}
