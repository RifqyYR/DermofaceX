package com.makassar.dermofacex

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makassar.dermofacex.data.Resource
import com.makassar.dermofacex.data.response.ClassificationProbabilityResponse
import com.makassar.dermofacex.ui.fragments.CameraFragment
import com.makassar.dermofacex.ui.viewModel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraFragmentTest {

    private lateinit var fragment: CameraFragment
    private lateinit var mockViewModel: MainViewModel
    private lateinit var probabilityFlow: MutableStateFlow<Resource<out ClassificationProbabilityResponse>>

    @Before
    fun setUp() {
        // Create a spyk of CameraFragment for partial mocking
        fragment = spyk(CameraFragment())

        // Mock ViewModel and set up a MutableStateFlow
        mockViewModel = mockk(relaxed = true)
        probabilityFlow =
            MutableStateFlow(Resource.Empty() as Resource<out ClassificationProbabilityResponse>)
        every { mockViewModel.probability } returns probabilityFlow
    }

    @Test
    fun getClassificationResult_hidesButton_whenLoading_andShows_whenDone() {
        // Launch the fragment using FragmentScenario
        val scenario = launchFragmentInContainer<CameraFragment>()

        scenario.onFragment { fragment ->
            // Mock a ViewModel and set up the probability flow
            val viewModel = mockk<MainViewModel>(relaxed = true)
            val probabilityFlow =
                MutableStateFlow<Resource<out ClassificationProbabilityResponse>>(Resource.Empty())
            every { viewModel.probability } returns probabilityFlow

            // Emit Loading state and verify visibility
            probabilityFlow.value = Resource.Loading()
            fragment.getClassificationResult()
            assertEquals(View.GONE, fragment.binding.prgBarButton.visibility)
            assertEquals(View.VISIBLE, fragment.binding.btnTakePicture.visibility)

            // Emit Success state and verify visibility
            probabilityFlow.value = Resource.Success(mockk())
            fragment.getClassificationResult()
            assertEquals(View.GONE, fragment.binding.prgBarButton.visibility)
            assertEquals(View.VISIBLE, fragment.binding.btnTakePicture.visibility)
        }
    }
}