

## LayoutInjector 简介

LayoutInjector 是一个用于Activity、Fragment和Dialog注入布局的注入器。



<br/>

## 前言

由于ButterKnife只提供了View的绑定，并没有提供Activity等布局的注入，所以这里特意写了一个注入器解决这个问题，原理也是使用了注解处理器



<br/>

## 使用方法

#### 一、配置

1. 在项目的根目录 `build.gradle` 下添加 jitpack 的路径：

   ```
   allprojects {
       repositories {
           ...
           maven { url 'https://jitpack.io' }
       }
   }
   ```

   

2. 在需要使用 LayoutInjector 的模块的 `build.gradle` 中添加以下依赖：

   ```
   implementation 'com.github.ytempest.layoutinjector:layoutinject:1.0'
   annotationProcessor 'com.github.ytempest.layoutinjector:compiler:1.0'
   ```



<br/>

#### 二、代码使用

1. 在 `Activity` 中使用

   ```
   @InjectLayout(R.layout.activity_main)
   public class MainActivity extends AppCompatActivity {
       @Override
       protected void onCreate(@Nullable Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           LayoutInjector.inject(this);
       }
   }
   ```

2. 在 `Fragment` 中使用

   ```
   @InjectLayout(R.layout.frag_home)
   public class HomeFragment extends Fragment {
       @Nullable
       @Override
       public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           return LayoutInjector.inject(this, inflater, container);
       }
   }
   ```

3. 在 `Dialog` 中使用

   ```
   @InjectLayout(R.layout.dialog_loading)
   public class LoadingDialog extends Dialog {
       public LoadingDialog(@NonNull Context context) {
           super(context);
           LayoutInjector.inject(this);
       }
   }
   ```



<br/>

## 更新记录

- 1.0 ：支持 `Activity`、`Fragment` 和 `Dialog` 的布局注入
- 1.1 ：添加 `Layout` 布局名称扫描机制，完善逻辑



<br/>