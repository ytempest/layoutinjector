

## LayoutInjector 简介

LayoutInjector 是一个用于Activity、Fragment和Dialog注入布局的注入器。



<br/>

## 前言

由于ButterKnife只提供了View的绑定，并没有提供Activity等布局的注入，所以这里特意写了一个注入器解决这个问题，原理也是使用了注解处理器



<br/>

## 使用方法

#### 一、配置

1. 在需要使用 LayoutInjector 的模块的 `build.gradle` 中添加以下依赖：

   ```
   implementation 'com.ytempest:layoutinjector:1.0'
   annotationProcessor 'com.ytempest:layoutinjector-compiler:1.0'
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

4. 在其他类中使用

   ```
   @InjectLayout(R.layout.layout_view_option)
   public class AbsViewOption {
   
       public void onStart() {
           int layoutId = LayoutInjector.getLayoutId(this);
           // TODO layoutId
       }
   }
   ```

   

   

<br/>

## 更新记录

- 1.0 ：支持 `Activity`、`Fragment` 和 `Dialog` 的布局注入，添加 `Layout` 布局名称扫描机制

  



<br/>