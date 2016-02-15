package com.aduinaja.aduinaja;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.View;


import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.api.RetrofitApi;
import network.api.RetrofitApiSingleton;
import network.model.Category;
import retrofit2.Call;


/**
 * Created by Triyandi on 08/02/2016.
 */
public class TabFragment extends AppCompatActivity {

    private FloatingActionButton fab;

    RetrofitApi api;
    List<String> categoryTitleList = new ArrayList<>();

    private ViewPager viewPager;
    private FragmentPagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_pager);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);*/
        RetrofitApiSingleton.getInstance().init("http://adm.aduinaja.com");
        api = RetrofitApiSingleton.getInstance().getApi();

        initViews();

        new GetCategory().execute(new Void[0]);


    }

    private void initViews(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    public void runThis(View view ){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TabFragment.this, VerifikasiNIK.class);
                startActivity(intent);
            }
        });
    }

    private class GetCategory extends AsyncTask<Void, Void, Call<Category>>{

        @Override
        protected Call<Category> doInBackground(Void... params) {
            Call<Category> callCategory = api.getCategoryList();
            try {
                Category category = callCategory.execute().body();
                for(int i = 0 ; i<category.getCategoryDatumList().size(); i++){
                    categoryTitleList.add(category.getCategoryDatumList().get(i).getNama());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return callCategory;
        }

        @Override
        protected void onPostExecute(Call<Category> aVoid) {
            super.onPostExecute(aVoid);
            FragmentPagerItems pages = new FragmentPagerItems(TabFragment.this);
            for (String title : categoryTitleList) {
                pages.add(FragmentPagerItem.of(title, LihatAduan.class, new Bundler().putString("category", title).get()));
            }

            adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), pages
            );

            viewPager.setAdapter(adapter);

            SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(viewPager);
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}
