/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.robotium.solo.Solo;

import hk.ust.comp4521.UasT.MainActivity;
import hk.ust.comp4521.UasT.R;

public class TestMainActivity extends
		ActivityInstrumentationTestCase2<MainActivity>
{

	public TestMainActivity()
	{
		super(MainActivity.class);
	}

	//MainActivity mActivity;

	public void testMain()
	{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(1000);
		solo.clickOnText("Course");
		solo.sleep(2000);

		solo.clickOnText("ACCT1010");
		solo.sleep(2000);
		solo.clickOnView(solo.getView(R.id.favorite));
		solo.sleep(2000);
		solo.clickOnView(solo.getView(R.id.favorite));
		solo.sleep(2000);

		assertEquals("Comments", getActivity().getTitle());
		assertTrue(solo.searchText("Overview of accounting"));

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "User0");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("User0");
		solo.sleep(2000);
        solo.clickOnActionBarItem(R.id.del);

        solo.clickOnText("TEST0");
        solo.sleep(2000);
        assertTrue(solo.searchText("ForTest"));
        solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_BACK);
		solo.sleep(2000);

		assertEquals("Comments", getActivity().getTitle());

		solo.sendKey(KeyEvent.KEYCODE_BACK);
		solo.sleep(2000);

		assertEquals("Courses", getActivity().getTitle());
	}

	public void testCourses()
	{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(2000);
		solo.clickOnText("Course");
		solo.sleep(2000);

		solo.clickOnActionBarItem(R.id.search);
		solo.typeText(0, "comp4521");

		solo.hideSoftKeyboard();
		solo.clickOnText("Embedded System");

		solo.sleep(2000);
		assertEquals("Comments", getActivity().getTitle());
		assertTrue(solo.searchText("TEST0"));

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "Testing");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(3000);

		solo.clickOnText("Testing");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.del);

		solo.clickOnText("Comments");
		solo.sleep(2000);
		solo.clickOnText("Groups");
		solo.sleep(2000);

        // Create Group
		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "Testing");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

        // Invite Others
		solo.clickOnText("Testing");
		solo.sleep(2000);
        solo.clickOnActionBarItem(R.id.add);
        solo.sleep(2000);
        solo.typeText(0, "yhliaa");
        solo.sleep(1000);
        solo.clickOnActionBarItem(R.id.confirm);
        solo.sleep(2000);

        // Kick Others
        solo.clickOnText("Testing");
        solo.sleep(2000);
        solo.clickOnText("Delete");
        solo.sleep(2000);

        // Join Group
        solo.clickOnText("HenryGroup");
        solo.sleep(2000);

        solo.clickOnActionBarItem(R.id.add);
        solo.sleep(2000);

        solo.clickOnText("HenryGroup");
        solo.sleep(2000);
        solo.clickOnActionBarItem(R.id.del);

        // Switch to Sharing
		solo.clickOnText("Groups");
		solo.sleep(2000);

		solo.clickOnText("Sharing");
		solo.sleep(2000);

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "Testing");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("Testing");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.del);

		solo.clickOnText("Sharing");
		solo.sleep(2000);

		solo.clickOnText("Trading");
		solo.sleep(2000);

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "Testing");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("Testing");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.del);

		solo.clickOnText("Trading");
		solo.sleep(2000);
	}

	public void testCalendar()
	{

		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(1000);
		solo.clickOnText("Calendar");
		solo.sleep(2000);
		solo.clickOnText("New");
		solo.sleep(2000);
		solo.clickOnText("Create");
		solo.sleep(3000);
		solo.clickOnText("NEW_EVENT", 1 , false);
		solo.sleep(2000);
		solo.clickOnText("Delete");
		solo.sleep(2000);

	}

	public void testA()
	{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(1000);

		solo.sendKey(Solo.MENU);

		solo.clickOnText("Login");
		solo.sleep(2000);
		solo.clearEditText(0);
		solo.typeText(0, "thlauac@ust.hk");
		solo.clickOnText("Login");
		solo.sleep(2000);
		solo.typeText(1, "test");
		solo.sleep(2000);
		solo.clearEditText(3);
		solo.typeText(3, "Darren Lau");
		solo.sleep(2000);
		solo.clickOnText("Set");
		solo.sleep(2000);
		solo.clickOnText("Validate");
		solo.sleep(2000);

	}

	public void testChat()
	{

		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(1000);

		solo.sendKey(Solo.MENU);

		solo.clickOnText("Login");
		solo.clearEditText(0);
		solo.typeText(0, "thlauac@ust.hk");
		solo.clickOnText("Login");
		solo.sleep(2000);
		solo.typeText(1, "test");
		solo.sleep(2000);
		solo.clickOnText("Validate");
		solo.sleep(2000);

		solo.clickOnText("ACCT1010");
		solo.sleep(2000);

		solo.clickOnText("TEST0");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.contact);
		solo.sleep(3000);
		solo.clickOnActionBarItem(R.id.match);
		solo.sleep(2000);
        solo.clickOnText("Show");
        solo.sleep(2000);
        assertFalse(solo.searchText("0E"));
        solo.clickOnText("Exit");
        solo.sleep(2000);

	}

	public void testChat01()
	{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(1000);
		solo.clickOnText("Chat");
		solo.sleep(2000);
		solo.clickOnText("wycheungae");
		solo.sleep(2000);
        solo.typeText(0, "Testing1");
        solo.sleep(2000);
	}

    public void testChatWithFriend() {
        Solo solo = new Solo(getInstrumentation(), getActivity());
        solo.sleep(1000);
        solo.clickOnText("Chat");
        solo.sleep(2000);
        solo.clickOnActionBarItem(R.id.add);
        solo.sleep(2000);
        solo.typeText(0, "yhliaa");
        solo.sleep(2000);
        solo.clickOnActionBarItem(R.id.confirm);
        solo.sleep(2000);
    }

}
