//package laiyi.tobacco.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.OrientationHelper;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import laiyi.tobacco.R;
//import laiyi.tobacco.adapter.PhotoAdapter;
//import me.iwf.photopicker.PhotoPickerActivity;
//import me.iwf.photopicker.utils.PhotoPickerIntent;
//
//public class TestActivity extends BaseActivity
//{
//
//    RecyclerView recyclerView;
//    PhotoAdapter photoAdapter;
//
//    ArrayList<String> selectedPhotos = new ArrayList<> ();
//
//    public final static int REQUEST_CODE = 1;
//
//
//    @Override
//    protected void onCreate (Bundle savedInstanceState)
//    {
//        super.onCreate (savedInstanceState);
//        setContentView (R.layout.activity_test);
//
//        recyclerView = (RecyclerView) findViewById (R.id.recycler_view);
//        photoAdapter = new PhotoAdapter (this, selectedPhotos);
//
//        recyclerView.setLayoutManager (new StaggeredGridLayoutManager (4, OrientationHelper.VERTICAL));
//        recyclerView.setAdapter (photoAdapter);
//
//        //选择相册并且不让使用拍照
//        findViewById (R.id.button_no_camera).setOnClickListener (new View.OnClickListener ()
//        {
//            @Override
//            public void onClick (View v)
//            {
//                PhotoPickerIntent intent = new PhotoPickerIntent (activity);
//                //图片数量控制
//                intent.setPhotoCount (3);
//                intent.setShowCamera (false);
//                startActivityForResult (intent, REQUEST_CODE);
//            }
//        });
//    }
//
//    public void previewPhoto (Intent intent)
//    {
//        startActivityForResult (intent, REQUEST_CODE);
//    }
//
//
//    @Override
//    protected void onActivityResult (int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult (requestCode, resultCode, data);
//
//        List<String> photos = null;
//        if ( resultCode == RESULT_OK && requestCode == REQUEST_CODE )
//        {
//            if ( data != null )
//            {
//                photos = data.getStringArrayListExtra (PhotoPickerActivity.KEY_SELECTED_PHOTOS);
//            }
//            selectedPhotos.clear ();
//
//            if ( photos != null )
//            {
//
//                selectedPhotos.addAll (photos);
//            }
//            photoAdapter.notifyDataSetChanged ();
//        }
//    }
//
//}
