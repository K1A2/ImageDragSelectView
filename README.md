# ImageDragSelectView
![Test GIF](https://github.com/K1A2/ImageDragSelectView/blob/master/selectview_test.gif)


This custom View make possible to crop image with dragging.
 
 이 커스텀 뷰는 사진에서 특정 영역을 드래그로 잘라내고 싶을때 사용하는 뷰 입니다.
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
