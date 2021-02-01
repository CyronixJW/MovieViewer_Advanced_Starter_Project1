package com.nyp.sit.movieviewer_intermediate_starter_project

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.nyp.sit.movieviewer_intermediate_starter_project", appContext.packageName)
    }


    @Test
    fun checkJsonMovie(){


        var jsonStr = "{ \"page\": 1,\n" +
                "  \"total_results\": 2,\n" +
                "  \"total_pages\": 1,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"vote_count\": 2427,\n" +
                "      \"id\": 297802,\n" +
                "      \"video\": false,\n" +
                "      \"vote_average\": 6.9,\n" +
                "      \"title\": \"Aquaman\",\n" +
                "      \"popularity\": 549.381,\n" +
                "      \"poster_path\": \"\\/5Kg76ldv7VxeX9YlcQXiowHgdX6.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Aquaman\",\n" +
                "      \"genre_ids\": [\n" +
                "        28,\n" +
                "        14,\n" +
                "        878,\n" +
                "        12\n" +
                "      ],\n" +
                "      \"backdrop_path\": \"\\/5A2bMlLfJrAfX9bqAibOL2gCruF.jpg\",\n" +
                "      \"adult\": false,\n" +
                "      \"overview\": \"Arthur Curry learns that he is the heir to the underwater kingdom of Atlantis, and must step forward to lead his people and be a hero to the world.\",\n" +
                "      \"release_date\": \"2018-12-07\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"vote_count\": 700,\n" +
                "      \"id\": 424783,\n" +
                "      \"video\": false,\n" +
                "      \"vote_average\": 6.6,\n" +
                "      \"title\": \"Bumblebee\",\n" +
                "      \"popularity\": 343.295,\n" +
                "      \"poster_path\": \"\\/fw02ONlDhrYjTSZV8XO6hhU3ds3.jpg\",\n" +
                "      \"original_language\": \"en\",\n" +
                "      \"original_title\": \"Bumblebee\",\n" +
                "      \"genre_ids\": [\n" +
                "        28,\n" +
                "        12,\n" +
                "        878\n" +
                "      ],\n" +
                "      \"backdrop_path\": \"\\/8bZ7guF94ZyCzi7MLHzXz6E5Lv8.jpg\",\n" +
                "      \"adult\": false,\n" +
                "      \"overview\": \"On the run in the year 1987, Bumblebee finds refuge in a junkyard in a small Californian beach town. Charlie, on the cusp of turning 18 and trying to find her place in the world, discovers Bumblebee, battle-scarred and broken.  When Charlie revives him, she quickly learns this is no ordinary yellow VW bug.\",\n" +
                "      \"release_date\": \"2018-12-15\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        var listitems = movieDBJsonUtils.getMovieDetailsFromJson(appContext, jsonStr)

        assertTrue(listitems!=null)
        assertEquals(2, listitems?.size)
        assertEquals(2427,listitems!!.get(0).vote_count)
        assertEquals(297802,listitems!!.get(0).id)
        assertEquals(false,listitems!!.get(0).video)
        assertEquals(6.9,listitems!!.get(0).vote_average,0.0)
        assertEquals("Aquaman",listitems!!.get(0).title)
        assertEquals(549.381,listitems!!.get(0).popularity,0.0)
        assertEquals("/5Kg76ldv7VxeX9YlcQXiowHgdX6.jpg",listitems!!.get(0).poster_path)
        assertEquals("en",listitems!!.get(0).original_langauge)
        assertEquals("Aquaman",listitems!!.get(0).original_title)
        assertEquals("[28,14,878,12]",listitems!!.get(0).genre_ids)
        assertEquals("/5A2bMlLfJrAfX9bqAibOL2gCruF.jpg",listitems!!.get(0).backdrop_path)
        assertEquals(false,listitems!!.get(0).adult)
        assertEquals("Arthur Curry learns that he is the heir to the underwater kingdom of Atlantis, and must step forward to lead his people and be a hero to the world.",listitems!!.get(0).overview)
        assertEquals("2018-12-07",listitems!!.get(0).release_date)
        assertEquals("Bumblebee",listitems!!.get(1).title)


    }
}