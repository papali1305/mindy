import { View, Text } from 'react-native'
import React, { useEffect } from 'react'
import { SplashScreen, Stack } from 'expo-router'
import { useFonts } from 'expo-font'


SplashScreen.preventAutoHideAsync()

const RootLayout = () => {
  const [ fontsLoaded, error] = useFonts({
    "Segoe UI Bold Italic": require("../assets/fonts/Segoe UI Bold Italic.ttf"),
    "Segoe UI Bold": require("../assets/fonts/Segoe UI Bold.ttf"),
    "Segoe UI Italic": require("../assets/fonts/Segoe UI Italic.ttf"),
    "Segoe UI": require("../assets/fonts/Segoe UI.ttf"),
  })

  useEffect(() => {
    if (fontsLoaded) SplashScreen.hideAsync()

    if (error) console.log(error)
  }, [fontsLoaded, error])

  if (!fontsLoaded && !error) {
    return null
  }

  return (
    <Stack>
      {/* <Stack.Screen name="index" options={{ headerShown: false}}/> */}
      <Stack.Screen name="(auth)" options={{ headerShown: false}}/>
    </Stack>
  )
}

export default RootLayout