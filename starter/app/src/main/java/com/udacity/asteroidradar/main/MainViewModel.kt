package com.udacity.asteroidradar.main

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private val _picOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _picOfTheDay

    private val _picOfTheDayCaption = MutableLiveData<String>()
    val pictureOfDayCaption: LiveData<String>
        get() = _picOfTheDayCaption

    private val _picOfTheDayErrorImageholder = MutableLiveData<Int>()
    val pictureOfDayImageHolder: LiveData<Int>
        get() = _picOfTheDayErrorImageholder

    private val _asteriods = MutableLiveData<List<Asteroid>>()
    val asteriods: LiveData<List<Asteroid>>
        get() = _asteriods

    private val _asteroidListErrorMessage = MutableLiveData<String>("")
    val asteroidListErrorMessage: LiveData<String>
        get() = _asteroidListErrorMessage

    val showAsteroidListErrorMessage = Transformations.map(asteroidListErrorMessage) {
        if (it.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    init {
        getPictureOfTheDay()
        getNearEarthObjects()
    }


    /**
     * GET picture of the day
     */
    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                //TODO: figure out a way to not having api_key as query param
                val result = NasaApi.nasaApiService.getPictureOfTheDay(apiKey = API_KEY)
                _picOfTheDay.value = result
                _picOfTheDayCaption.value = result.title
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Getting error calling GetPictureOfTheDay:\n ${e.message}"
                )
                _picOfTheDayErrorImageholder.value = R.drawable.ic_broken_image
                _picOfTheDayCaption.value = R.string.error_unable_to_load_pod.toString()
            }
        }
    }

    /**
     * GET a list of Near-Earth Objects
     */
    private fun getNearEarthObjects() {
        viewModelScope.launch {
            try {
                var result = NasaApi.nasaApiService.getNearEarthObjects(apiKey = API_KEY)
                Log.d(TAG, result)
                // TODO: REMOVE this
//                var result =
//                    "{\"links\":{\"next\":\"http://www.neowsapp.com/rest/v1/feed?start_date=2021-03-08&end_date=2021-03-08&detailed=false&api_key=DEMO_KEY\",\"prev\":\"http://www.neowsapp.com/rest/v1/feed?start_date=2021-03-06&end_date=2021-03-06&detailed=false&api_key=DEMO_KEY\",\"self\":\"http://www.neowsapp.com/rest/v1/feed?start_date=2021-03-07&end_date=2021-03-07&detailed=false&api_key=DEMO_KEY\"},\"element_count\":11,\"near_earth_objects\":{\"2021-03-14\":[{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/3449135?api_key=DEMO_KEY\"},\"id\":\"3449135\",\"neo_reference_id\":\"3449135\",\"name\":\"(2009 EM1)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=3449135\",\"absolute_magnitude_h\":23.0,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0667659413,\"estimated_diameter_max\":0.1492931834},\"meters\":{\"estimated_diameter_min\":66.7659413495,\"estimated_diameter_max\":149.2931834393},\"miles\":{\"estimated_diameter_min\":0.0414864197,\"estimated_diameter_max\":0.0927664547},\"feet\":{\"estimated_diameter_min\":219.0483710172,\"estimated_diameter_max\":489.807047955}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 19:51\",\"epoch_date_close_approach\":1615146660000,\"relative_velocity\":{\"kilometers_per_second\":\"11.870209774\",\"kilometers_per_hour\":\"42732.7551863482\",\"miles_per_hour\":\"26552.4722434284\"},\"miss_distance\":{\"astronomical\":\"0.1311338652\",\"lunar\":\"51.0110735628\",\"kilometers\":\"19617346.918787124\",\"miles\":\"12189654.1335613512\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/3600564?api_key=DEMO_KEY\"},\"id\":\"3600564\",\"neo_reference_id\":\"3600564\",\"name\":\"(2012 EP3)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=3600564\",\"absolute_magnitude_h\":25.5,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0211132445,\"estimated_diameter_max\":0.0472106499},\"meters\":{\"estimated_diameter_min\":21.113244479,\"estimated_diameter_max\":47.2106498806},\"miles\":{\"estimated_diameter_min\":0.0131191578,\"estimated_diameter_max\":0.0293353287},\"feet\":{\"estimated_diameter_min\":69.2691770164,\"estimated_diameter_max\":154.8905885541}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 12:29\",\"epoch_date_close_approach\":1615120140000,\"relative_velocity\":{\"kilometers_per_second\":\"10.4183834247\",\"kilometers_per_hour\":\"37506.1803288012\",\"miles_per_hour\":\"23304.8818826376\"},\"miss_distance\":{\"astronomical\":\"0.0961463565\",\"lunar\":\"37.4009326785\",\"kilometers\":\"14383290.140660655\",\"miles\":\"8937362.062420839\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/3745993?api_key=DEMO_KEY\"},\"id\":\"3745993\",\"neo_reference_id\":\"3745993\",\"name\":\"(2016 EM84)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=3745993\",\"absolute_magnitude_h\":23.3,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.058150704,\"estimated_diameter_max\":0.130028927},\"meters\":{\"estimated_diameter_min\":58.1507039646,\"estimated_diameter_max\":130.0289270043},\"miles\":{\"estimated_diameter_min\":0.0361331611,\"estimated_diameter_max\":0.0807962044},\"feet\":{\"estimated_diameter_min\":190.7831555951,\"estimated_diameter_max\":426.6041048727}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 03:39\",\"epoch_date_close_approach\":1615088340000,\"relative_velocity\":{\"kilometers_per_second\":\"15.0746160848\",\"kilometers_per_hour\":\"54268.617905222\",\"miles_per_hour\":\"33720.4087200531\"},\"miss_distance\":{\"astronomical\":\"0.4265912939\",\"lunar\":\"165.9440133271\",\"kilometers\":\"63817148.927983993\",\"miles\":\"39654137.5570573834\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/3798998?api_key=DEMO_KEY\"},\"id\":\"3798998\",\"neo_reference_id\":\"3798998\",\"name\":\"(2018 CH)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=3798998\",\"absolute_magnitude_h\":26.4,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0139493823,\"estimated_diameter_max\":0.0311917671},\"meters\":{\"estimated_diameter_min\":13.9493822934,\"estimated_diameter_max\":31.1917670523},\"miles\":{\"estimated_diameter_min\":0.0086677416,\"estimated_diameter_max\":0.0193816595},\"feet\":{\"estimated_diameter_min\":45.7656914036,\"estimated_diameter_max\":102.3351970157}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 10:09\",\"epoch_date_close_approach\":1615111740000,\"relative_velocity\":{\"kilometers_per_second\":\"8.4524495808\",\"kilometers_per_hour\":\"30428.8184908874\",\"miles_per_hour\":\"18907.2844672961\"},\"miss_distance\":{\"astronomical\":\"0.1529633024\",\"lunar\":\"59.5027246336\",\"kilometers\":\"22882984.227205888\",\"miles\":\"14218827.0630137344\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/3843819?api_key=DEMO_KEY\"},\"id\":\"3843819\",\"neo_reference_id\":\"3843819\",\"name\":\"(2019 RD2)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=3843819\",\"absolute_magnitude_h\":23.6,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0506471459,\"estimated_diameter_max\":0.1132504611},\"meters\":{\"estimated_diameter_min\":50.6471458835,\"estimated_diameter_max\":113.2504610618},\"miles\":{\"estimated_diameter_min\":0.0314706677,\"estimated_diameter_max\":0.0703705522},\"feet\":{\"estimated_diameter_min\":166.1651821003,\"estimated_diameter_max\":371.5566426699}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 08:45\",\"epoch_date_close_approach\":1615106700000,\"relative_velocity\":{\"kilometers_per_second\":\"23.2837053398\",\"kilometers_per_hour\":\"83821.339223195\",\"miles_per_hour\":\"52083.3204745458\"},\"miss_distance\":{\"astronomical\":\"0.4780838354\",\"lunar\":\"185.9746119706\",\"kilometers\":\"71520323.457270598\",\"miles\":\"44440668.2551783324\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/54111270?api_key=DEMO_KEY\"},\"id\":\"54111270\",\"neo_reference_id\":\"54111270\",\"name\":\"(2021 CN4)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=54111270\",\"absolute_magnitude_h\":20.7,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.1925550782,\"estimated_diameter_max\":0.4305662442},\"meters\":{\"estimated_diameter_min\":192.5550781879,\"estimated_diameter_max\":430.566244241},\"miles\":{\"estimated_diameter_min\":0.1196481415,\"estimated_diameter_max\":0.2675413778},\"feet\":{\"estimated_diameter_min\":631.7424027221,\"estimated_diameter_max\":1412.6189567557}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 09:49\",\"epoch_date_close_approach\":1615110540000,\"relative_velocity\":{\"kilometers_per_second\":\"7.1674848891\",\"kilometers_per_hour\":\"25802.9456006462\",\"miles_per_hour\":\"16032.9469483571\"},\"miss_distance\":{\"astronomical\":\"0.113707517\",\"lunar\":\"44.232224113\",\"kilometers\":\"17010402.34618879\",\"miles\":\"10569773.890993702\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/54117601?api_key=DEMO_KEY\"},\"id\":\"54117601\",\"neo_reference_id\":\"54117601\",\"name\":\"(2021 CC8)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=54117601\",\"absolute_magnitude_h\":24.5,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0334622374,\"estimated_diameter_max\":0.0748238376},\"meters\":{\"estimated_diameter_min\":33.4622374455,\"estimated_diameter_max\":74.8238376074},\"miles\":{\"estimated_diameter_min\":0.0207924639,\"estimated_diameter_max\":0.0464933628},\"feet\":{\"estimated_diameter_min\":109.7842471007,\"estimated_diameter_max\":245.4850393757}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 00:27\",\"epoch_date_close_approach\":1615076820000,\"relative_velocity\":{\"kilometers_per_second\":\"6.3319572245\",\"kilometers_per_hour\":\"22795.0460081837\",\"miles_per_hour\":\"14163.9551154739\"},\"miss_distance\":{\"astronomical\":\"0.1616062615\",\"lunar\":\"62.8648357235\",\"kilometers\":\"24175952.499063005\",\"miles\":\"15022240.292379269\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/54125415?api_key=DEMO_KEY\"},\"id\":\"54125415\",\"neo_reference_id\":\"54125415\",\"name\":\"(2021 DJ2)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=54125415\",\"absolute_magnitude_h\":24.27,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.037201031,\"estimated_diameter_max\":0.0831840342},\"meters\":{\"estimated_diameter_min\":37.201031034,\"estimated_diameter_max\":83.1840342251},\"miles\":{\"estimated_diameter_min\":0.0231156419,\"estimated_diameter_max\":0.0516881465},\"feet\":{\"estimated_diameter_min\":122.0506306575,\"estimated_diameter_max\":272.9135068469}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 01:57\",\"epoch_date_close_approach\":1615082220000,\"relative_velocity\":{\"kilometers_per_second\":\"7.3481502685\",\"kilometers_per_hour\":\"26453.3409666912\",\"miles_per_hour\":\"16437.0773356643\"},\"miss_distance\":{\"astronomical\":\"0.1174380191\",\"lunar\":\"45.6833894299\",\"kilometers\":\"17568477.514379317\",\"miles\":\"10916545.7204838946\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/54129170?api_key=DEMO_KEY\"},\"id\":\"54129170\",\"neo_reference_id\":\"54129170\",\"name\":\"(2021 EJ)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=54129170\",\"absolute_magnitude_h\":25.708,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0191846984,\"estimated_diameter_max\":0.0428982898},\"meters\":{\"estimated_diameter_min\":19.1846984222,\"estimated_diameter_max\":42.8982897999},\"miles\":{\"estimated_diameter_min\":0.0119208152,\"estimated_diameter_max\":0.0266557532},\"feet\":{\"estimated_diameter_min\":62.9419259715,\"estimated_diameter_max\":140.742425107}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 21:10\",\"epoch_date_close_approach\":1615151400000,\"relative_velocity\":{\"kilometers_per_second\":\"10.1097512087\",\"kilometers_per_hour\":\"36395.1043514892\",\"miles_per_hour\":\"22614.5024788462\"},\"miss_distance\":{\"astronomical\":\"0.0136364068\",\"lunar\":\"5.3045622452\",\"kilometers\":\"2039977.411733516\",\"miles\":\"1267583.1850378808\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/54130488?api_key=DEMO_KEY\"},\"id\":\"54130488\",\"neo_reference_id\":\"54130488\",\"name\":\"(2021 EO1)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=54130488\",\"absolute_magnitude_h\":26.188,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.0153799519,\"estimated_diameter_max\":0.0343906179},\"meters\":{\"estimated_diameter_min\":15.3799518778,\"estimated_diameter_max\":34.3906178894},\"miles\":{\"estimated_diameter_min\":0.0095566561,\"estimated_diameter_max\":0.0213693326},\"feet\":{\"estimated_diameter_min\":50.4591613187,\"estimated_diameter_max\":112.8301147962}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 07:27\",\"epoch_date_close_approach\":1615102020000,\"relative_velocity\":{\"kilometers_per_second\":\"8.9277156533\",\"kilometers_per_hour\":\"32139.7763519453\",\"miles_per_hour\":\"19970.4071449074\"},\"miss_distance\":{\"astronomical\":\"0.0140326739\",\"lunar\":\"5.4587101471\",\"kilometers\":\"2099258.125844593\",\"miles\":\"1304418.5127096634\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false},{\"links\":{\"self\":\"http://www.neowsapp.com/rest/v1/neo/54131389?api_key=DEMO_KEY\"},\"id\":\"54131389\",\"neo_reference_id\":\"54131389\",\"name\":\"(2021 EA4)\",\"nasa_jpl_url\":\"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=54131389\",\"absolute_magnitude_h\":22.188,\"estimated_diameter\":{\"kilometers\":{\"estimated_diameter_min\":0.097040936,\"estimated_diameter_max\":0.2169901294},\"meters\":{\"estimated_diameter_min\":97.0409359504,\"estimated_diameter_max\":216.9901293853},\"miles\":{\"estimated_diameter_min\":0.0602984234,\"estimated_diameter_max\":0.1348313737},\"feet\":{\"estimated_diameter_min\":318.3757843034,\"estimated_diameter_max\":711.9098960923}},\"is_potentially_hazardous_asteroid\":false,\"close_approach_data\":[{\"close_approach_date\":\"2021-03-07\",\"close_approach_date_full\":\"2021-Mar-07 04:18\",\"epoch_date_close_approach\":1615090680000,\"relative_velocity\":{\"kilometers_per_second\":\"18.3453635907\",\"kilometers_per_hour\":\"66043.3089264073\",\"miles_per_hour\":\"41036.7438159668\"},\"miss_distance\":{\"astronomical\":\"0.2417601402\",\"lunar\":\"94.0446945378\",\"kilometers\":\"36166802.024821374\",\"miles\":\"22473008.6909640012\"},\"orbiting_body\":\"Earth\"}],\"is_sentry_object\":false}]}}"

                val parsedResult = parseAsteroidsJsonResult(JSONObject(result))
                if (parsedResult.isNullOrEmpty()) {
                    Log.w(TAG, "Parsed asteriod is empty!!")
                } else {
                    Log.i(TAG, "We have ${parsedResult.size} Asteriod")
                    _asteroidListErrorMessage.value = ""
                    _asteriods.value = parsedResult.subList(0, 1)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Getting error calling 'getNearEarthObjects'\n ${e.message}")
                _asteroidListErrorMessage.value =
                    "Getting error calling 'getNearEarthObjects'\n ${e.message}"
                _asteriods.value = listOf() // empty list
            }
        }
    }
}