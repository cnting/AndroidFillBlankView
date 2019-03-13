<img src='https://img.shields.io/bintray/v/cnting77/maven/FillBlankView.svg'/>

# AndroidFillBlankView 填空题
参考 https://github.com/TMLAndroid/FillBlankDemo

## 使用方式
1. 添加依赖
```gradle
implementation 'com.cnting:fillblankview:version'
```
2. 布局中添加
```xml
<com.cnting.fillblankview.FillBlankView
            android:layout_width="match_parent"
            app:fill_text="A group of ____(sheep) are eating grass and ____(leaf) in front of the farm."
            app:fill_split="____"
            app:underline_focus_color="@android:color/holo_red_dark"
            app:underline_unfocus_color="@android:color/holo_blue_dark"
            app:underline_fixed_width="true"
            android:id="@+id/fillBlankView1"
            app:underline_fixed_width_size="100dp"
            android:layout_height="wrap_content">

        <TextView
                android:layout_width="wrap_content" android:layout_height="wrap_content"
                android:textColor="@android:color/black"
                android:textSize="16sp"/>
</com.cnting.fillblankview.FillBlankView>
```
## 方法介绍
#### 1. 想要固定下划线长度？  
在布局中设置
```xml
app:underline_fixed_width="true"
app:underline_fixed_width_size="100dp"
```

#### 2. 想要根据答案确定下划线长度？
调用`FillBlankTagUtil.blankToHtml(rightAnswers)`转化成html
```kotlin
private fun init2() {
    val answer1 = FillBlankTagUtil.blankToHtml(listOf("sheep"))
    val answer2 = FillBlankTagUtil.blankToHtml(listOf("leaves"))
    val content =
        "A group of $answer1(sheep) are eating grass and $answer2 (leaf) in front of the farm."
    fillBlankView2.setFillContent(content, "")
}
```

#### 3. 想要插入图片？
调用`FillBlankTagUtil.imageToHtml(R.mipmap.ic_launcher)`转化成html
```kotlin
private fun init3() {
    val image = FillBlankTagUtil.imageToHtml(R.mipmap.ic_launcher)
    val content =
        "A group of ____ are eating grass and ____ (leaf) in front of the farm.$image"
    fillBlankView3.setFillContent(content, "____")
}
```

#### 4. 根据用户填写内容判断对错？
```kotlin
private fun init4() {
    val answer1 = FillBlankTagUtil.blankToHtml(listOf("sheep", "a sheep"), "sheep")
    val answer2 = FillBlankTagUtil.blankToHtml(listOf("leaves", "leaffff"), "leafs")
    val content =
        "A group of $answer1(sheep) are eating grass and $answer2 (leaf) in front of the farm."
    fillBlankView4.setFillContent(content, "")
    fillBlankView4.showAnswerResult(true)
    fillBlankView4.isEnabled = false
}
```

demo示例如下：  
<img src='https://github.com/cnting/AndroidFillBlankView/blob/master/resources/demo.jpg' width='300'>


感谢：https://github.com/TMLAndroid/FillBlankDemo
