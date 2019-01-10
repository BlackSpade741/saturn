package utoronto.saturn.app.front_end.views;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

import utoronto.saturn.Event;
import utoronto.saturn.User;
import utoronto.saturn.app.GuiManager;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.listeners.OnCategoryItemClickListener;
import utoronto.saturn.app.front_end.listeners.OnItemClickListener;
import utoronto.saturn.app.front_end.viewmodels.MainViewModel;

public class MainActivity extends BaseView implements OnItemClickListener, OnCategoryItemClickListener {
    private MainViewModel mViewModel;

    private Fragment curFragment;
    private HomeFragment homeFragment;
    private DiscoverFragment discoverFragment;
    private MineFragment mineFragment;
    private Map<String, CategoryFragment> categoryFragments;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        homeFragment = HomeFragment.newInstance();
        discoverFragment = DiscoverFragment.newInstance();
        mineFragment = MineFragment.newInstance();

        categoryFragments = new HashMap<>();
        for (String category: GuiManager.getCategories()) {
            categoryFragments.put(category, CategoryFragment.newInstance(category));
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (curFragment == null) {
            fragmentTransaction.add(R.id.container_fragment, homeFragment);
        } else {
            fragmentTransaction.replace(R.id.container_fragment, homeFragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        curFragment = homeFragment;
        setNavMenuListener();
    }

    private void replaceFragment(Fragment fragment) {
        if (curFragment != fragment) {
            FragmentTransaction trans = fragmentManager.beginTransaction();
            trans.replace(R.id.container_fragment, fragment);
            trans.addToBackStack(null);
            trans.commit();
            curFragment = fragment;
        }
    }

    private void setNavMenuListener(){
        BottomNavigationView navMenu = findViewById(R.id.nav_menu);

        navMenu.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            replaceFragment(homeFragment);
                            break;
                        case R.id.menu_discover:
                            replaceFragment(discoverFragment);
                            break;
                        case R.id.menu_mine:
                            replaceFragment(mineFragment);
                            break;
                    }
                    return true;
                });
    }

    @Override
    public void onItemClick(Object item) {
        if (item instanceof Event) {
            eventPopUp((Event) item);
        } else if (item instanceof User) {
            artistPopUp((User) item);
        }
    }

    @Override
    public void onCategoryItemClick(String category) {
        switch (category) {
            case "Anime":
                replaceFragment(categoryFragments.get("Anime"));
                break;
            case "Concerts":
                replaceFragment(categoryFragments.get("Concerts"));
                break;
            case "Games":
                replaceFragment(categoryFragments.get("Games"));
                break;
            case "Movies":
                replaceFragment(categoryFragments.get("Movies"));
                break;
        }
    }
}
