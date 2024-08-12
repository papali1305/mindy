import { View, Text, ActivityIndicator, SafeAreaView } from 'react-native'
import React, { useEffect, useState } from 'react'
import OpenImageGuess from '../../components/Exercises/OpenImageGuess'
import { useLessonContext } from '../../context/LessonProvider'
import { getGames } from '../api/fetch'
import { useGlobalContext } from '../../context/GlobalProvider'
import { Redirect } from 'expo-router'

const lesson = () => {
  const { curExerciseIndex, setCurExerciseIndex, exercises, setExercises } = useLessonContext()
  const { curLesson, isTablet } = useGlobalContext()
  const [loadingExercises, setLoadingExercises] = useState(false)

  const loadExercises = async () => {
    try {
      setLoadingExercises(true)

      let lessonExercises = await getGames(curLesson.id)
      if (!lessonExercises) throw Error("the games could not be loaded")

      setExercises([...lessonExercises].map((ex, index) => ({ ...ex, exerciseId: index + 1 })))
    } catch (error) {
      console.error(error)
    } finally {
      setLoadingExercises(false)
    }
  }

  const correctExercise = (passed) => {
    // console.log("passed is" , passed)
    // if (!passed) {
    //   // get the current exercise and change its Id to the last element of the list note that the indexes start from 1
    //   let current = exercises[curExerciseIndex]
    //   current.exercisId = exercises.length

    //   // add the failed exercise at the end of the list
    //   exs = exercises
    //   exs.push({...current, exerciseId: exercises.length + 1})
    //   setExercises(exs)
    // }

    if (curExerciseIndex == exercises.length) {
      setCurExerciseIndex(0)
      setExercises(null)
      return <Redirect href='/congrats' />
    }
    setCurExerciseIndex(curExerciseIndex + 1)
  }

  const ready = () => {
    return exercises.length > 0
  }

  const renderExercise = () => {
    const gameType = exercises[curExerciseIndex].type
    if (gameType == "SCENARIO") {
      return <OpenImageGuess lesson={curLesson} handleCorrectExercise={correctExercise} exercise={exercises[curExerciseIndex]} />
    }
  }

  const renderLoading = () => (
    <>
      <ActivityIndicator size="large" color="black" />
      <Text className={`${isTablet ? 'text-[18px]' : 'text-xl'}`}>Loading exercises...</Text>
    </>
  )

  useEffect(() => {
    loadExercises()
  }, [])

  return (
    <SafeAreaView className="flex-col items-center justify-center h-full">
      {ready() ? (
        renderExercise()
      ) : (
        renderLoading()
      )}
    </SafeAreaView>
  )
}

export default lesson