package hk.ust.comp4521.exust.test;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hk.ust.comp4521.exust.R;
import hk.ust.comp4521.exust.*;
import hk.ust.comp4521.exust.data.Course;
import hk.ust.comp4521.exust.data.Database;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;

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
		solo.typeText(0, "Jason");
		solo.typeText(1, "Testing 1234");

		solo.clickOnActionBarItem(R.id.confirm);
		solo.sleep(2000);

		solo.clickOnText("Jason");
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
		solo.typeText(0, "skchanae@ust.hk");
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
		solo.typeText(0, "skchanae@ust.hk");
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
		solo.clickOnText("cochung");
		solo.sleep(2000);
	}

}
