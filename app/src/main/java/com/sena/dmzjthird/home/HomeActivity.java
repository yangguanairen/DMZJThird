package com.sena.dmzjthird.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ActivityHomeBinding;
import com.sena.dmzjthird.utils.FixFragmentNavigator;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_home);
        final NavController navController = NavHostFragment.findNavController(fragment);


        FixFragmentNavigator fixFragmentNavigator = new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
        navController.getNavigatorProvider().addNavigator(fixFragmentNavigator);

        NavGraph navGraph = initNavGraph(navController.getNavigatorProvider(), fixFragmentNavigator);
        navController.setGraph(navGraph);

        binding.navView.setOnNavigationItemSelectedListener(item -> {
                    navController.navigate(item.getItemId());
                    return true;
                });


        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator navigator) {

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        FragmentNavigator.Destination destination1 = navigator.createDestination();
        destination1.setId(R.id.navigation_comic);
        destination1.setClassName(ComicFragment.class.getCanonicalName());
        navGraph.addDestination(destination1);

        navGraph.setStartDestination(R.id.navigation_comic);

        FragmentNavigator.Destination destination2 = navigator.createDestination();
        destination2.setId(R.id.navigation_topic);
        destination2.setClassName(TopicFragment.class.getCanonicalName());
        navGraph.addDestination(destination2);

        FragmentNavigator.Destination destination3 = navigator.createDestination();
        destination3.setId(R.id.navigation_novel);
        destination3.setClassName(NovelFragment.class.getCanonicalName());
        navGraph.addDestination(destination3);

        FragmentNavigator.Destination destination4 = navigator.createDestination();
        destination4.setId(R.id.navigation_account);
        destination4.setClassName(AccountFragment.class.getCanonicalName());
        navGraph.addDestination(destination4);


        return navGraph;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}