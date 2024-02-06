package com.example.wisebuy.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wisebuy.MyApplication;
import com.example.wisebuy.R;
import com.example.wisebuy.adapters.CategoryAdapter;
import com.example.wisebuy.adapters.HomeScreenDealsAdapter;
import com.example.wisebuy.adapters.SliderAdapter;
import com.example.wisebuy.databinding.FragmentHomeBinding;
import com.example.wisebuy.repositories.HomeRepository;
import com.example.wisebuy.viewModels.HomeViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SliderAdapter sliderAdapter;
    private SliderView sliderView;
    CategoryAdapter categoryAdapter,updatedAdapter;
    HomeScreenDealsAdapter dealsAdapter;
    private RecyclerView categoryView,topdealsView;
    HomeViewModel homeViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
         categoryView = root.findViewById(R.id.categoryView);
        categoryView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        topdealsView=root.findViewById(R.id.topDealsView);
        topdealsView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        homeViewModel = myApplication.getHomeViewModel();
        categoryAdapter = new CategoryAdapter(requireContext(),new ArrayList<>());
        dealsAdapter=new HomeScreenDealsAdapter(requireContext(),new ArrayList<>());


        homeViewModel.setAdapter(categoryAdapter);
        homeViewModel.setDealadapter(dealsAdapter);
        homeViewModel.loadCategories();

        homeViewModel.getAdapterUpdateLiveData().observe(getViewLifecycleOwner(), adapterUpdate -> {
            if (adapterUpdate) {
                updatedAdapter = homeViewModel.getAdapter();
               categoryView.setAdapter(updatedAdapter);
            }
        });

        homeViewModel.loadTopDeals();

        homeViewModel.getDealAdapterLiveData().observe(getViewLifecycleOwner(), adapterUpdate -> {
            if (adapterUpdate) {
                dealsAdapter = homeViewModel.getDealadapter();
                topdealsView.setAdapter(dealsAdapter);
            }
        });




        sliderAdapter =new SliderAdapter(getContext());
        sliderView=root.findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
         HomeRepository.getOffers(offers -> {
            sliderAdapter.renewItems(offers);

        },e -> {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
