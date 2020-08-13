package test;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;

import com.ntech.fourtop.R;
import com.ntech.fourtop.ui.login.LoginFragment;

import org.junit.Test;

class NavigationTest {

    @Test
    public void testNavigationToLogin() {
        TestNavHostController navHostController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.navigation);

        FragmentScenario<LoginFragment> loginFragment =
                FragmentScenario.launchInContainer(LoginFragment.class);
        loginFragment.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), navHostController));
    }
}
