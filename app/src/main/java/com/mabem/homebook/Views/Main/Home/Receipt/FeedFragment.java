package com.mabem.homebook.Views.Main.Home.Receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.mabem.homebook.Adapters.FeedAdapter;
import com.mabem.homebook.Model.Home;
import com.mabem.homebook.Model.Member;
import com.mabem.homebook.Model.Receipt;
import com.mabem.homebook.R;
import com.mabem.homebook.Utils.NavigationDrawer;
import com.mabem.homebook.ViewModels.HomeViewModel;
import com.mabem.homebook.Views.Main.Home.Reminder.RemindersFragment;
import com.mabem.homebook.databinding.FragmentFeedBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FeedFragment extends Fragment {

    private static final String FEED_FRAGMENT_TAG = "Feed Fragment";
    private static String home_id = "";
    private final ArrayList<Receipt> list = new ArrayList<>();
    private FragmentFeedBinding fragmentFeedBinding;
    private HomeViewModel homeViewModel;
    private FeedAdapter adapter;
    private NavController navController;
    private Home currentHome;
    private Member currentMember;

    public static void setHome_id(String home_id2) {
        home_id = home_id2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((NavigationDrawer) getActivity()).disableNavDrawer();

        fragmentFeedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);

        fragmentFeedBinding.myReceipts.setHasFixedSize(true);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        homeViewModel.getCurrentMember().observe(getViewLifecycleOwner(), member -> {
            if (member != null) {
                currentMember = member;
                HashMap<Home, Boolean> memberHomes = member.getHome_role();
                for (Home h1 : memberHomes.keySet()) {
                    if (h1.getId().equals(home_id)) {
                        boolean isAdmin = memberHomes.get(h1);
                        homeViewModel.updateCurrentHome(h1.getId());
                        homeViewModel.getCurrentHome().observe(getViewLifecycleOwner(), h -> {
                            if (h != null) {
                                currentHome = h;
                                list.clear();
                                ArrayList<Receipt> receipts = h.getReceipts();
                                list.addAll(receipts);
                                Collections.sort(list, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                                adapter = new FeedAdapter(getContext(), list, isAdmin, member.getId());
                                fragmentFeedBinding.myReceipts.setAdapter(adapter);
                            }
                        });
                    }
                }


            }
        });


        fragmentFeedBinding.addButton.setOnClickListener(v -> {
            ReceiptManageFragment.setToEditFlag(false);
            Navigation.findNavController(v).navigate(R.id.action_feedFragment_to_addReceiptFragment);
        });

        setHasOptionsMenu(true);
        return fragmentFeedBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.overflow_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homeInfoFragment) {
            if (currentMember.isThisMemberAdmin(currentHome)) {
                navController.navigate(R.id.editHomeFragment);
                return true;
            }
        }
        if (item.getItemId() == R.id.remindersFragment) {
            RemindersFragment.setHome_id(home_id);
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}