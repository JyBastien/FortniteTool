package utils;

import com.google.android.material.tabs.TabLayout;
/*pour enlver du code les méthodes overrider inutilsées*/
public class AbstractOnTabSelectedListener implements TabLayout.OnTabSelectedListener{
    @Override
    public void onTabSelected(TabLayout.Tab tab) {}
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}
}
