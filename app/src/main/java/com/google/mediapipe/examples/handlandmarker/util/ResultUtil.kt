package com.google.mediapipe.examples.handlandmarker.util

import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

object ResultUtil {
    /**
     * 逻辑：
     * 首先调用setAFrameToList方法设置单帧Array
     * 然后调用addFrameToList 方法将一帧的内容加入List中
     * 最后外界通过getList方法经历上述过程取出list
     * @author zzy
     * */
    val numKeyPoints: Int = 21
    val numCoords: Int = 3
    val inputShape: IntArray = intArrayOf(1,50,258)
    val inputBuffer: TensorBuffer = TensorBuffer.createFixedSize(inputShape, DataType.FLOAT32)
    val rightHandCoords: FloatArray = FloatArray(numKeyPoints * numCoords)
    val leftHandCoords: FloatArray = FloatArray(numKeyPoints * numCoords)
    val PoseCoords: FloatArray = FloatArray(33* 4){ 0.0f }
    private var handCoordinatesList: MutableList<FloatArray> = mutableListOf()


    // 一些常量
    private const val TAG = "MediaPipeResult"
    private const val NUMFRAME = 50
    private const val NUMKEYPOINTS = 21
    private const val NUMHANDS = 2
    private const val NUMAXIS = 3
    private var result: HandLandmarkerResult? = null
    // 所用到的列表
    private val frameFloatArray: FloatArray = FloatArray(NUMKEYPOINTS * NUMAXIS * NUMHANDS)
    private val twoHandsResultList: MutableList<Float> = mutableListOf()
    fun gettwoHandsResultList(): MutableList<Float> {
        return twoHandsResultList
    }
    fun getList(): MutableList<Float> {
        addFrameToList()
        return twoHandsResultList
    }


    fun setResults(
        handLandmarkerResults: HandLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        result = handLandmarkerResults

    }
    lateinit var kads: GestureRecognizerResult
    fun setAFrameToList(result: HandLandmarkerResult) {
        var i = 0
        if (result.handednesses()[0][0].displayName().equals("Left")) {
            for (landmark in result.landmarks()) {
                for (normalizedLandmark in landmark) {
                    twoHandsResultList[i] = normalizedLandmark.x()
                    i++
                    twoHandsResultList[i] = normalizedLandmark.y()
                    i++
                    twoHandsResultList[i] = normalizedLandmark.z()
                    i++
                }
            }
        } else {
            for (landmark in result.landmarks().reversed()) {
                for (normalizedLandmark in landmark) {
                    twoHandsResultList[i] = normalizedLandmark.x()
                    i++
                    twoHandsResultList[i] = normalizedLandmark.y()
                    i++
                    twoHandsResultList[i] = normalizedLandmark.z()
                    i++
                }
            }
        }


//        var i = 0
//        for (landmark in result.landmarks()) {
//            for (normalizedLandmark in landmark) {
//                twoHandsResultList[i] = normalizedLandmark.x()
//                i++
//                twoHandsResultList[i] = normalizedLandmark.y()
//                i++
//                twoHandsResultList[i] = normalizedLandmark.z()
//                i++
//            }
//        }
    }

     fun addFrameToList() {
        for (i in 1..NUMHANDS* NUMKEYPOINTS* NUMAXIS){
            twoHandsResultList.removeFirst()
        }
        frameFloatArray.forEach {
            twoHandsResultList.add(it)
        }
    }
    init {
        for ( i in  1..NUMFRAME* NUMHANDS* NUMKEYPOINTS* NUMAXIS){
            twoHandsResultList.add(0f)
        }
    }
}