# ImageDragSelectView
![Test GIF](https://github.com/K1A2/ImageDragSelectView/blob/master/selectview_test_2.gif)


This custom View make possible to crop image with dragging.
 
 이 커스텀 뷰는 사진에서 특정 영역을 드래그로 잘라내고 싶을때 사용하는 뷰 입니다. 이 뷰의 가장 큰 장점은 한번에 한번이 아닌 여러개의 부분을 잘라서 각각 따로따로 리턴이 가능하다는 점 입니다.
 # 사용법
 1. build.gradle에 다음과 같이 추가합니다.
 ```
 allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
 
 
 2. dependency를 추가합니다.
 ```
 dependencies {
 	implementation 'com.github.K1A2:ImageDragSelectView:{version}'
}
```


xml에 다음과 같이 추가합니다.
```xml
<com.k1a2.imagedragselectview.ImageDragSelectView
            android:layout_width="match_parent"
            android:layout_weight="1"
            android:layout_height="match_parent"
            android:id="@+id/custom_view"/>
```


코드애서 id를 통해 **ImageDragSelectView**를 가져옵니다.
```java
private ImageDragSelectView imageDragSelectView = null;
...
imageDragSelectView = findViewById(R.id.imageDragSelectView);
```

## Functions
### setLineColor(int color)
드래그 할때 그려지는 라인의 색상을 정합니다. 기본은 반투명한  하얀색 입니다.
### setLineWidth(float lineWidth)
드래그 할때 그려지는 라인의 두께를 정합니다. 기본은 10입니다.
### setSourceImage(String path), setSourceImage(Bitmap bitmap)
드래그 해서 자를 비트맵을 파일경로 또는 비트맵 형싣으로 지정 가능합니다.
### getCropBitmap(int position)
선택 영역이 여러개인 경우 몇번째로 그려진 부분을 잘라올지 int로 지정하면 해당 부분을 잘라서 리턴해 줍니다. 그린 갯수보다 큰 수를 입력하면 null이 리턴됩니다.
### getCropBitmaps()
자를 부분이 여러개 지정되 있다면모든 영역을 한번에 리턴합니다.
### getSectionCount()
몇개의 부분이 그려졌는지 갯수를 리턴합니다.

# License
*****Free*****
