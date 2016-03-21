Android设计模式简介
1、适配器模式：ListView或GridView的Adapter简介：不同的数据提供者使用一个适配器来向一个相同的客户提供服务。
2、建造者模式：AlertDialog.Builder简介：可以分步地构造每一部分。
3、命令模式：Handler.post后Handler.handleMessage简介：把请求封装成一个对象发送出去，方便定制、排队、取消。
4、享元模式：Message.obtainMessage通过重用Message对象来避免大量的Message对象被频繁的创建和销毁。简介：运用共享技术有效地支持大量细粒度的对象。
5、迭代器模式：如通过Hashtable.elements方法可以得到一个Enumeration，然后通过这个Enumeration访问Hashtable中的数据，而不用关心Hashtable中的数据存放方式。简介：提供一个方法顺序访问数据集合中的所有数据而又不暴露对象的内部表示。
6、备忘录模式：Activity的onSaveInstanceState和onRestoreInstanceState就是通过Bundle这种序列化的数据结构来存储Activity的状态，至于其中存储的数据结构，这两个方法不用关心简介：不需要了解对象的内部结构的情况下备份对象的状态，方便以后恢复。
7、观察者模式：我们可以通过BaseAdapter.registerDataSetObserver和BaseAdapter.unregisterDataSetObserver两方法来向BaseAdater注册、注销一个DataSetObserver。这个过程中，DataSetObserver就是一个观察者，它一旦发现BaseAdapter内部数据有变量，就会通过回调方法DataSetObserver.onChanged和DataSetObserver.onInvalidated来通知DataSetObserver的实现类。事件通知也是观察者模式简介：一个对象发生改变时，所有信赖于它的对象自动做相应改变。
8、原型模式：比如我们需要一张Bitmap的几种不同格式：ARGB_8888、RGB_565、ARGB_4444、ALAPHA_8等。那我们就可以先创建一个ARGB_8888的Bitmap作为原型，在它的基础上，通过调用Bitmap.copy(Config)来创建出其它几种格式的Bitmap。另外一个例子就是Java中所有对象都有的一个名字叫clone的方法，已经原型模式的代名词了简介：在系统中要创建大量的对象，这些对象之间具有几乎完全相同的功能，只是在细节上有一点儿差别。
9、代理模式：类似于ios开发的delegate委托模式，所有的AIDL都一个代理模式的例子。假设一个Activity A去绑定一个Service S，那么A调用S中的每一个方法其实都是通过系统的Binder机制的中转，然后调用S中的对应方法来做到的。Binder机制就起到了代理的作用。简介：为其他对象提供一种代理以控制对这个对象的访问。
10、状态模式：View.onVisibilityChanged方法，就是提供了一个状态模式的实现，允许在View的visibility发生改变时，引发执行onVisibilityChanged方法中的动作。简介：状态发生改变时，行为改变。
11、策略模式：举例：Java.util.List就是定义了一个增（add）、删（remove）、改（set）、查（indexOf）策略，至于实现这个策略的ArrayList、LinkedList等类，只是在具体实现时采用了不同的算法。但因为它们策略一样，不考虑速度的情况下，使用时完全可以互相替换使用。简介：定义了一系列封装了算法、行为的对象，他们可以相互替换。
12、调解者模式简介：一个对象的某个操作需要调用N个对象的M个方法来完成时，把这些调用过程封装起来，就成了一个调解者举例：如Resource.getDrawable方法的实现逻辑是这样的：创建一个缓存来存放所有已经加载过的，如果getDrawable中传入的id所对应的Drawable以前没有被加载过，那么它就会根据id所对应的资源类型，分别调用XML解析器生成，或者通过读取包中的图片资源文件来创建Drawable。而Resource.getDrawable把涉及到多个对象、多个逻辑的操作封装成一个方法，就实现了一个调解者的角色。
13、抽象工厂模式DAO与Service的使用

建造者模式
建造者模式最明显的标志就是Build类，而在Android中最常用的就是Dialog的构建，Notification的构建也是标准的建造者模式。
建造者模式很好理解，如果一个类的构造需要很多参数，而且这些参数并不都是必须的，那么这种情况下就比较适合Builder。
比如构建一个AlertDialog，标题、内容、取消按钮、确定按钮、中立按钮，你可能只需要单独设置几个属性即可；另外在我的OkHttpPlus项目中，构造一个Http请求也是这样的，有可能你只需要设置URL，有可能需要添加请求参数、Http Header等，这个时候建造者模式也是比较合适的。
单例模式
单例在Android开发中经常用到，但是表现形式可能不太一样。
以ActivityManager等系统服务来说，是通过静态代码块的形式实现单例，在首次加载类文件时，生成单例对象，然后保存在Cache中，之后的使用都是直接从Cache中获取。
class ContextImpl extends Context {

    static {
        registerService(ACTIVITY_SERVICE, new ServiceFetcher() {
                public Object createService(ContextImpl ctx) {
                    return new ActivityManager(ctx.getOuterContext(),         ctx.mMainThread.getHandler());
                }});
    }
}
当然，还有更加明显的例子，比如AccessibilityManager内部自己也保证了单例，使用getInstance获取单例对象。
 public static AccessibilityManager getInstance(Context context) {
        synchronized (sInstanceSync) {
            if (sInstance == null) {

               ......

                IBinder iBinder = ServiceManager.getService(Context.ACCESSIBILITY_SERVICE);
                IAccessibilityManager service = iBinder == null
                        ? null : IAccessibilityManager.Stub.asInterface(iBinder);
                sInstance = new AccessibilityManager(context, service, userId);
            }
        }
        return sInstance;
    }
除此之外，还有一些伪单例，比如Application，默认情况下在一个进程中只存在一个实例，但是Application不能算是单例，因为它的构造方法未私有，你可以生成多个Application实例，但是没有用，你没有通过attach()绑定相关信息，没有上下文环境。
public Application() {
        super(null);
    }
单例的使用场景也很简单，就是一个App只需要存在一个类实例的情况，或者是类的初始化操作比较耗费资源的情况。在很多开源框架中，我们只需要一个对象即可完成工作，比如各种网络框架和图片加载库。
除此之外，因为单例的实现方式很多，比如懒汉式、饿汉式、静态内部类、双重锁检查、枚举等方式，所以要清楚每种实现方式的主要特点和使用场景。
原型模式
原型模式在开发中使用的并不多，但是在源码中却有所体现。
书中以Intent介绍了原型模式，是通过实现Cloneable接口来做的
public class Intent implements Parcelable, Cloneable {
     @Override
        public Object clone() {
            return new Intent(this);
        }
    }
其实这样来看的话，原型模式也比较好理解，就是你想更快的获取到一个相同属性的对象，那么就可以使用原型模式，比如这里就获取到了一个Intent对象，Intent里面的属性与被clone的相同，但是两者并无关联，可以单独使用。
除了实现Cloneable接口，你完全可以自己定义一个方法，来获取一个对象。我这里以PhoneLayoutInflater为例子介绍。
PhoneLayoutInflater是LayoutInflater的子类，如果我们在Activity中获取LayoutInflate的话，是通过下面方法
 @Override public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return mInflater;
        }
        return getBaseContext().getSystemService(name);
    }
可以看到，如果为null，就会调用cloneInContext()，这个方法在LayoutInflate是抽象方法，具体实现在PhoneLayoutInflater中
  public LayoutInflater cloneInContext(Context newContext) {
        return new PhoneLayoutInflater(this, newContext);
    }
可以看到，这也是一个原型模式，所以我们不要太纠结于形式，更重要的是理解这样做的好处。
除了在源码中可以看到原型模式，在开源框架中也可以看到，比如OkHttpClient中就存在着下面的方法
/** Returns a shallow copy of this OkHttpClient. */
  @Override public OkHttpClient clone() {
    return new OkHttpClient(this);
  }
可以看到，实现和前面的完全相同，也是new了一个对象返回，因为OkHttpClient的构造过程比较复杂，参数众多，所以用这种方式来直接生成新对象，成本很低，而且能保留之前对象的参数设置。
工厂方法模式
书中对于工厂方法模式的一个观点很新奇，就是Activity.onCreate()可以看做是工厂方法模式，来生成不同的View对象填充界面。
但是我对这个说法不太苟同，原因有两点：一是这种形式不太符合工厂方法，没有抽象，没有实现，不符合一般格式，也不是静态方法，不可看做是静态工厂方法；二是没有以生成对象为结果，即不是return view来生成对象，只是通过setContentView()来设置了属性而已。这就像是给一个Activity设置了背景颜色一样。当然，设计模式这东西一个人有一个人的看法。
静态工厂方法在Android中比较明显的例子应该就是BitmapFactory了，通过各种decodeXXX()就可以从不同渠道获得Bitmap对象，这里不再赘述。
策略模式
在书中策略模式讲得非常好，结合动画的插值器用法，我们可以很好的理解策略模式的形式和用法。
在我看来，策略模式就相当于一个影碟机，你往里面插什么碟子，就能放出什么电影。
同样，在OkHttpPlus的封装中，为了对网络返回值进行解析，我使用了策略模式。当然我写代码的时候还不知道策略模式，是写完了之后突然想到，这就是策略模式啊！
策略模式的精髓就在于，你传入一个类，后面的处理就能按照这个类的实现去做。以动画为例，设置不同的插值器对象，就可以得到不同的变化曲线；以返回值解析为例，传入什么样的解析器，就可以把二进制数据转换成什么格式的数据，比如String、Json、XML。
责任链模式
书中对于责任链模式选取的例子非常有代表性，那就是Android的触摸机制，这个看法让我从另一个维度去理解Android中的触摸事件传递。
我在这里提到这个模式，并不想说太多，只是简单的推荐你读一下这一章的内容，相信你也会有收获的。
观察者模式
Android中的观察者模式应该是用的非常频繁的一种模式了，对于这个模式的使用场景就一句话：你想在某个对象发生变化时，立刻收到通知。
书中介绍观察者模式使用的是ListView的Adapter为例子，我之前知道Adapter属于适配器模式，不知道这里还有观察者模式的身影，学到了。
Android里面的各种监听器，也都属于观察者模式，比如触摸、点击、按键等，ContentProvider和广播接收者也有观察者模式的身影，可以说是无处不在。
除此之外，现在很多基于观察者模式的第三方框架也是非常多，比如EventBus、RxJava等等，都是对观察者模式的深入使用，感兴趣的同学可以研究一下。
模板方法模式
这个模式我之前见的比较少，但是理解之后，就会发现这个模式很简单。
我觉得，模板方法模式的使用场景也是一句话：流程确定，具体实现细节由子类完成。
这里要关注一下『流程』这个关键字，随便拿一个抽象类，都符合"具体实现细节由子类完成"的要求，关键就在于是否有流程，有了流程，就叫模板方法模式，没有流程，就是抽象类的实现。
书中讲这个模式用的是AsyncTask，各个方法之间的执行符合流程，具体实现由我们完成，非常经典。
另外一个方面，Activity的生命周期方法可以看做是模板方法模式，各个生命周期方法都是有顺序的，具体实现我们可以重写，是不是和前面的要求很符合？关于这方面的理解，可以参考我的这篇文章：对Activity生命周期的理解。
除了Android里面的模板方法模式，在其他开源项目中也存在着这个模式的运用。比如鸿洋的OkHttp-Utils项目，就是模板方法模式的典型实现。将一个Http请求的过程分割成几部分，比如获取URL，获取请求头，拼接请求信息等步骤，这几个步骤之前有先后顺序，就可以这样来做。
代理模式和装饰器模式
之所以把这两个放在一起说，是因为这两种模式很像，所以这里简单介绍下他们之间的区别，主要有两点。

装饰器模式关注于在一个对象上动态的添加方法，而代理模式关注于控制对对象的访问
代理模式，代理类可以对它的客户隐藏一个对象的具体信息。因此，当使用代理模式的时候，我们常常在一个代理类中创建一个对象的实例。而当我们使用装饰器模式的时候，通常的做法是将原始对象作为一个参数传给装饰者的构造器

这两句话可能不太好理解，没关系，下面看个例子。
代理模式会持有被代理对象的实例，而这个实例一般是作为成员变量直接存在于代理类中的，即不需要额外的赋值。
比如说WindowManagerImpl就是一个代理类，虽然名字上看着不像，但是它代理的是WindowManagerGlobal对象。从下面的代码中就可以看出来。
public final class WindowManagerImpl implements WindowManager {
    private final WindowManagerGlobal mGlobal = WindowManagerGlobal.getInstance();
    private final Display mDisplay;
    private final Window mParentWindow;

    ......

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        mGlobal.addView(view, params, mDisplay, mParentWindow);
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        mGlobal.updateViewLayout(view, params);
    }

    @Override
    public void removeView(View view) {
        mGlobal.removeView(view, false);
    }

    @Override
    public void removeViewImmediate(View view) {
        mGlobal.removeView(view, true);
    }

    @Override
    public Display getDefaultDisplay() {
        return mDisplay;
    }
}
从上面的代码中可以看出，大部分WindowManagerImpl的方法都是通过WindowManagerGlobal实现的，而WindowManagerGlobal对象不需要额外的赋值，就存在于WindowManagerImpl中。另外，WindowManagerGlobal中其实有大量的方法，但是通过WindowManagerImpl代理之后，都没有暴露出来，对开发者是透明的。
我们再来看一下装饰器模式。装饰器模式的目的不在于控制访问，而是扩展功能，相比于继承基类来扩展功能，使用装饰器模式更加的灵活。
书中是以Context和它的包装类ContextWrapper讲解的，也非常的典型，我这里就不在赘述了，贴出一些代码来说明装饰器模式的形式。
public class ContextWrapper extends Context {
    Context mBase;

    public ContextWrapper(Context base) {
        mBase = base;
    }
}
但是还有一个问题，就是在ContextWrapper中，所有方法的实现都是通过mBase来实现的，形式上是对上号了，说好的扩展功能呢？
功能扩展其实是在ContextWrapper的子类ContextThemeWrapper里面。
在ContextWrapper里面，获取系统服务是直接通过mBase完成的
@Override
    public Object getSystemService(String name) {
        return mBase.getSystemService(name);
    }
但是在ContextThemeWrapper里面，对这个方法进行了重写，完成了功能扩展
@Override public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return mInflater;
        }
        return getBaseContext().getSystemService(name);
    }
当然，如果不存在功能扩展就不算是装饰器模式了吗？其实设计模式本来就是『仁者见仁，智者见智』的事情，只要你能理解这个意思就好。
外观模式
外观模式可能看到的比较少，但是其实不经意间你就用到了。
这里以我的一个开源项目KLog来说吧，在最开始写这个类的时候，就只有KLog这一个类，完成基本的Log打印功能，后来又添加了JSON解析、XML解析、Log信息存储等功能，这个时候一个类就不太合适了，于是我把JSON、XML、FILE操作相关的代码抽取到单独的类中，比如JSON打印的代码
public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(KLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(KLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        Util.printLine(tag, true);
        message = headString + KLog.LINE_SEPARATOR + message;
        String[] lines = message.split(KLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        Util.printLine(tag, false);
    }
}
代码很简单，就一个方法，但是在使用的时候，无论打印哪种格式，都是这样使用的
//普通打印
 KLog.d(LOG_MSG);
 //JSON格式打印
 KLog.json(JSON);
 //XML格式打印
 KLog.xml(XML);
可以看到，虽然功能不同，但是都通过KLog这个类进行了封装，用户只知道用KLog这个类能完成所有需求即可，完全不需要知道代码实现是几个类完成的。
实际上，在KLog内部，是多个类共同完成打印功能的。
 private static void printLog(int type, String tagStr, Object... objects) {

        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                BaseLog.printDefault(type, tag, headString + msg);
                break;
            case JSON:
                JsonLog.printJson(tag, msg, headString);
                break;
            case XML:
                XmlLog.printXml(tag, msg, headString);
                break;
        }
    }
但是通过外观模式，这些细节对用户隐藏了，这样如果以后我想更换JSON的解析方式，用户的代码不需要任何改动，这也是这个设计模式的优势所在。
