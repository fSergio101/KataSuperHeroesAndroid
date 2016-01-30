package com.karumi.katasuperheroes;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.karumi.katasuperheroes.di.MainComponent;
import com.karumi.katasuperheroes.di.MainModule;
import com.karumi.katasuperheroes.model.SuperHero;
import com.karumi.katasuperheroes.model.SuperHeroesRepository;
import com.karumi.katasuperheroes.ui.view.SuperHeroDetailActivity;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.karumi.katasuperheroes.matchers.ToolbarMatcher.onToolbarWithTitle;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.when;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 30/1/16.
 */
@RunWith(AndroidJUnit4.class) @LargeTest public class SuperHeroDetailActivityTest {

  @Rule public DaggerMockRule<MainComponent> daggerRule =
      new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
          new DaggerMockRule.ComponentSetter<MainComponent>() {
            @Override public void setComponent(MainComponent component) {
              SuperHeroesApplication app =
                  (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                      .getTargetContext()
                      .getApplicationContext();
              app.setComponent(component);
            }
          });

  @Rule public IntentsTestRule<SuperHeroDetailActivity> activityRule =
      new IntentsTestRule<>(SuperHeroDetailActivity.class, true, false);

  @Mock SuperHeroesRepository repository;

  private SuperHeroDetailActivity startActivity(SuperHero superHero) {
    Intent intent = new Intent();
    intent.putExtra("super_hero_name_key", superHero.getName());
    return activityRule.launchActivity(intent);
  }

  @Test public void showsElemenAtScreenStart() {
    SuperHero superHero = createMockSuperHero();
    givenSuperHeroByName(superHero);
    startActivity(superHero);

    onToolbarWithTitle(superHero.getName()).check(matches(isDisplayed()));
    onView(withText(superHero.getDescription())).check(matches(isDisplayed()));
  }

  @Test public void showsSuperHeroDescription() {
    SuperHero superHero = createMockSuperHero();
    givenSuperHeroByName(superHero);
    startActivity(superHero);
    //scrollToView(R.id.tv_super_hero_name);

    onView(allOf(withId(R.id.tv_super_hero_name), withText(superHero.getName()))).check(
        matches(isDisplayed()));
  }

  private SuperHero createMockSuperHero() {
    return new SuperHero("Iron Man", "https://i.annihil.us/u/prod/marvel/i/mg/c/60/55b6a28ef24fa.jpg",
        true, "Wounded, captured and forced to build a weapon by his enemies, billionaire "
        + "industrialist Tony Stark instead created an advanced suit of armor to save his "
        + "life and escape captivity. Now with a new outlook on life, Tony uses his money "
        + "and intelligence to make the world a safer, better place as Iron Man.");
  }

  private void givenSuperHeroByName(SuperHero superHero) {
    when(repository.getByName(superHero.getName())).thenReturn(superHero);
  }

  private void scrollToView(int viewId) {
    onView(withId(viewId)).perform(scrollTo());
  }

}
