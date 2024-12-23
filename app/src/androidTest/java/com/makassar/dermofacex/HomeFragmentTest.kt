package com.makassar.dermofacex

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.makassar.dermofacex.data.DisorderInformation
import com.makassar.dermofacex.ui.fragments.HomeFragment
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeFragmentTest {

    private lateinit var mockNavController: NavController
    private val oilyInfo = DisorderInformation(
        "Berminyak",
        "Kulit berminyak disebabkan oleh produksi sebum berlebih dari kelenjar sebaceous yang bisa menyebabkan kulit tampak mengilap dan lebih rentan terhadap jerawat.",
        "Faktor genetik, perubahan hormon, atau penggunaan produk yang tidak cocok dengan jenis kulit.",
        listOf(
            "Bersihkan wajah dua kali sehari dengan pembersih berbasis air yang lembut.",
            "Gunakan toner dengan asam salisilat atau niacinamide untuk mengontrol minyak.",
            "Hindari produk berat atau berminyak; pilih pelembap ringan dan bebas minyak.",
            "Tetap gunakan pelembap untuk menjaga keseimbangan hidrasi kulit."
        ),
        "Menggunakan pembersih berbasis gel atau busa yang lembut, toner yang mengandung asam salisilat atau niacinamide, serta pelembap yang ringan dan non-komedogenik (tidak menyumbat pori-pori).",
        listOf(
            "Heavy oils (minyak berat): Minyak berat seperti minyak kelapa atau minyak zaitun cenderung menyumbat pori-pori dan memperparah minyak berlebih.",
            "Occlusive emollients: Bahan seperti petrolatum, lanolin, dan mineral oil bersifat sangat oklusif, sehingga bisa menutup pori-pori dan menyebabkan lebih banyak minyak dan jerawat.",
            "Alcohol (terutama yang mengeringkan): Denatured alcohol atau isopropyl alcohol dapat mengeringkan kulit secara berlebihan, yang memicu kulit memproduksi lebih banyak minyak sebagai kompensasi."
        )
    )

    @Before
    fun setUp() {
        // Initialize the NavController mock
        mockNavController = mockk(relaxed = true)
    }

    @Test
    fun cardOily_click_navigates_to_DetailFacialSkinDisorderFragment() {
        val scenario =
            launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_DermofaceX)

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)

            // Perform a click on the card
            fragment.binding.cardOily.card.performClick()
        }

        verify {
            mockNavController.navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                withArg { bundle ->
                    assertEquals(oilyInfo, bundle.get("disorderInformation"))
                    assertEquals(R.drawable.oily, bundle.get("image"))
                }
            )
        }
    }

    @Test
    fun btnApplicationInformation_click_navigates_to_AboutAppFragment() {
        // Launch HomeFragment in a test container
        val scenario =
            launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_DermofaceX)

        // Set the mock NavController on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Perform a click on btnApplicationInformation
        scenario.onFragment { fragment ->
            fragment.binding.btnApplicationInformation.btn.performClick()
        }

        // Verify that the correct navigation action is triggered
        verify {
            mockNavController.navigate(R.id.action_homeFragment_to_aboutAppFragment)
        }
    }

    @Test
    fun btnHowToUse_click_navigates_to_HowToUseFragment() {
        // Launch HomeFragment in a test container
        val scenario =
            launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_DermofaceX)

        // Set the mock NavController on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Perform a click on btnHowToUse
        scenario.onFragment { fragment ->
            fragment.binding.btnHowToUse.btn.performClick()
        }

        // Verify that the correct navigation action is triggered
        verify {
            mockNavController.navigate(R.id.action_homeFragment_to_howToUseFragment)
        }
    }

    @Test
    fun cardCheckYourFaceSkin_click_showsImageSourceDialog() {
        // Launch the HomeFragment in a test container
        val scenario =
            launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_DermofaceX)

        // Set the mock NavController on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Perform click on the cardCheckYourFaceSkin to open the dialog
        onView(withId(R.id.card_check_your_face_skin)).perform(click())

        // Verify that the dialog title is displayed
        onView(withText(R.string.select_image_source))
            .check(matches(isDisplayed()))

        // Verify that both options (Camera and Gallery) are displayed
        onView(withText(R.string.camera)).check(matches(isDisplayed()))
        onView(withText(R.string.gallery)).check(matches(isDisplayed()))
    }

    @Test
    fun imageSourceDialog_selectCamera_navigatesToCameraFragment() {
        val scenario =
            launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_DermofaceX)

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Open the dialog by clicking the cardCheckYourFaceSkin button
        onView(withId(R.id.card_check_your_face_skin)).perform(click())

        // Select the "Camera" option in the dialog
        onView(withText(R.string.camera)).perform(click())

        // Verify navigation to CameraFragment
        verify {
            mockNavController.navigate(R.id.action_homeFragment_to_cameraFragment)
        }
    }

    @Test
    fun imageSourceDialog_selectGallery_triggersGallerySelection() {
        // Create a spy before launching the fragment
        val spiedFragment = spyk<HomeFragment>(recordPrivateCalls = true)

        // Create custom fragment factory
        val factory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (className) {
                    HomeFragment::class.java.name -> spiedFragment
                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        // Launch with our custom factory
        val scenario = launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_DermofaceX,
            factory = factory
        )

        // Open the dialog by clicking the cardCheckYourFaceSkin button
        onView(withId(R.id.card_check_your_face_skin))
            .perform(scrollTo(), click())

        // Select the "Gallery" option in the dialog
        onView(withText(R.string.gallery))
            .perform(click())

        // Verify the call on our spy
        verify { spiedFragment.pickImageFromGallery() }
    }
}