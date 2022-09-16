# Module
学习Handle。是一套 Android 消息传递机制,主要用于线程间通信
作用：传递message/子线程通知主线程更新ui
子线程通知主线程更新ui三种使用方法：1，Handle.post.Handler.post(Runnable)其实就是生成一个what为0的Message,调用
2,handle的匿名子类。
3，静态弱引用handler子类（防止内存泄漏）
//避免内存泄露的方法：
//移除标记为0x1的消息
new Handler().removeMessages(0x1);
//移除回调的消息
new Handler().removeCallbacks(Runnable);
//移除回调和所有message
new Handler().removeCallbacksAndMessages(null);

学习eventBus的应用
EventBus是一个基于发布者/订阅者模式的事件总线框架。
发布者/订阅者模式，也就是观察者模式（定义了对象之间的一种一对多的依赖关系，当一个对象状态发生改变时，它的所有依赖者都会收到通知并自动更新）。在EventBus中，当发布者发布事件时，所有订阅该事件的事件处理方法将被调用。
EventBus的优点：
解耦和简化Activities, Fragments等组件以及后台线程之间的通信，分离事件发送方和接收方
使得代码更简洁，避免出现复杂的依赖性和生命周期问题
体积小（大概只有50k 的 jar包）
步骤：1，编写messageevent类。2，注册（订阅）事件。3，发布事件。
优先级、多种事件、注解

学习contentprovider的使用
其实很多时候我们用到ContentProvider并不是自己暴露自己的数据，更多的时候通过 ContentResolver来读取其他应用的信息，最常用的莫过于读取系统APP，信息，联系人， 多媒体信息等！
contentprovider/url/contentresolver
