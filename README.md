# MediaSelector
MediaSelector是一个针对于Android媒体库选择的框架，该风格完全仿照微信风格！市面虽然有很多，但是我感觉庞大！
所以自己动手封装一个！
## 框架特点
</br>1、自定义性扩展能力强（选择文件个数自定义；支持拍照展示）
</br>2、支持多图压缩
</br>3、两行代码解决所有问题，多一行算我输
</br>4、日后把裁剪、视频都加上
### 运行效果预览
![压缩运行效果预览](./MediaSelectorGif.gif)

```java


 /***
    *自定义选择图片方式
    */
 MediaSelector.MediaOptions mediaOptions = new MediaSelector.MediaOptions();
 //是否要显示拍照功能
 mediaOptions.isShowCamera = true;
 //是否要压缩
 mediaOptions.isCompress = false;
 //是否要显示视频文件（目前该版本没有加上该功能，日后期待）
 mediaOptions.isShowVideo = false;
 //Activity中
 MediaSelector.with(MainActivity.this).setMediaOptions(mediaOptions).openMediaActivity();
  //Fragment中
  MediaSelector.with(MainFragment.this).setMediaOptions(mediaOptions).openMediaActivity();

 /***
    *默认选择图片方式
    */
 //Activity中
  MediaSelector.with(MainActivity.this).openMediaActivity();
 //Fragment中
  MediaSelector.with(MainFragment.this).openMediaActivity();

  /**
       * 选择图片结果回调
       * @param requestCode
       * @param resultCode
       * @param data
       */
      @Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          //具体重写这行代码即可
          List<MediaSelectorFile> mediaList = MediaSelector.resultMediaFile(data);

          if (mediaList != null && mediaList.size() > 0) {
              mData.addAll(0, mediaList);
              mDataAdapter.notifyDataSetChanged();

          }
      }