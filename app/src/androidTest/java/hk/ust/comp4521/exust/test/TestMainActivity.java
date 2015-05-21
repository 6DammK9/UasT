package hk.ust.comp4521.exust.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.robotium.solo.Solo;

import hk.ust.comp4521.exust.MainActivity;
import hk.ust.comp4521.exust.R;

public class TestMainActivity extends
		ActivityInstrumentationTestCase2<MainActivity>
{

	public TestMainActivity()
	{
		super(MainActivity.class);
	}

	MainActivity mActivity;

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

		assertEquals("Comments", getActivity().getTitle());
		assertTrue(solo.searchText("Overview of accounting"));

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "User0");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("User0");
		solo.sleep(2000);

		// assertEquals("hello", getActivity().getTitle());
		assertTrue(solo.searchText("T"));

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
		// assertEquals("Comments", getActivity().getTitle());
		// assertTrue(solo.searchText("COMP4521 is an interesting course"));

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "Testing");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("Testing");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.del);

		solo.clickOnText("Comments");
		solo.sleep(2000);
		solo.clickOnText("Groups");
		solo.sleep(2000);

		solo.clickOnActionBarItem(R.id.add);
		solo.typeText(0, "Testing");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("Testing");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.del);

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
		solo.sleep(3000);
		solo.clickOnView(solo.getView(R.id.text, 23));
		solo.sleep(3000);
		solo.typeText(0, "Testing");
		solo.sleep(1000);
		solo.clickOnText("Submit");
		solo.sleep(1000);
		solo.clickOnView(solo.getView(R.id.text, 23));
		solo.sleep(3000);
		solo.clickOnText("Delete");
		solo.sleep(1000);
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
		solo.clearEditText(2);
		solo.typeText(2, "user");
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
		solo.clickOnText("test");
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.contact);
		solo.sleep(2000);
		solo.clickOnActionBarItem(R.id.match);
		solo.sleep(2000);
		solo.typeText(0, "testMessage");
		solo.sleep(2000);
		solo.clickOnText("Join");
		solo.sleep(2000);

		solo.clickOnView(solo.getView(R.id.send));
		solo.sleep(2000);
		solo.clickOnText("Match");
		solo.sleep(2000);

	}

	public void testChat01()
	{

		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep(1000);
		solo.clickOnText("Chat");
		solo.sleep(2000);
		solo.clickOnText("hehehehe");
		solo.sleep(2000);
	}

}
