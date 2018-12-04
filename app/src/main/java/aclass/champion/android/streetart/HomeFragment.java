package aclass.champion.android.streetart;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String[] imageUrls = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/e2c985ee-3758-44f6-a378-8469b4c47e97.jpg?alt=media&token=c8d01d33-fa90-4541-836b-918c06ff421d",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Short_North_Columbus_Ohio_CAPAAB0222.jpg?alt=media&token=c32699c9-adbf-4e0b-b97c-99af3a6b0747.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/56f020c09105842b008b7af5-750-563.jpg?alt=media&token=c738e335-473b-46bf-96c3-bc922ac5ac22",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/asdasdasdkljh123.jpg?alt=media&token=4ac23a87-fcf5-40c6-99ed-99d0c65a934d",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/jkhsdgafouiyer8976.jpg?alt=media&token=cc7b04c8-f54f-4220-a429-212856359819",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Messenger-Wall.jpg?alt=media&token=f594deae-5153-4154-9324-551d155e8ef0.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/IMG_4725.jpg?alt=media&token=69db89dc-bf49-4ac4-aeca-e5611dbf473c.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/IMG_4849.jpg?alt=media&token=a970f2f9-af84-41a0-a202-53cc19b90515.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-005.jpg?alt=media&token=76fe6d16-c230-4535-9dad-9de6d51c63ef.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Short_North_Columbus_Ohio_CAPAAB0216_2x3_72dpi.jpg?alt=media&token=dfc5bd16-141c-42e8-90f0-ac839dd90bb0.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-010.jpg?alt=media&token=3ead111e-bbf7-4e5a-9771-34065daabfaf.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-011.jpg?alt=media&token=acbc87f9-22c5-41a6-b265-bfa2e8f9cb32.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-016.jpg?alt=media&token=43b00914-f9ae-48ce-82c8-4adcaa55e08a.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-017.jpg?alt=media&token=fdc40c95-a989-4faa-8b1e-85b217cc0d42.png",
            "https://firebasestorage.googleapis.com/v0/b/streetart-14cdd.appspot.com/o/Columbus-Murals-018.jpg?alt=media&token=cc2db9a9-a953-4847-8dfa-8dd9d8578290.png"
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = v.findViewById(R.id.view_pager1);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), imageUrls);
        viewPager.setAdapter(adapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
