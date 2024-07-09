import { View, Text, SafeAreaView } from 'react-native'
import React, { useState } from 'react'
import { Link, router } from 'expo-router'
import CustomButton from '../../components/CustomButton'
import FormField from '../../components/FormField'

const login = () => {
  const [form, setForm] = useState({
    email: "",
    password: ""
  })


  return (
    <SafeAreaView className="h-full w-full flex-col items-center justify-center bg-white">
      <View className="w-[70%] py-8 h-fit">
        <Text className="font-suBold text-6xl text-slate-600 mb-3">Welcom Back!</Text>
        <View className="d-flex flex-row justify-start gap-1">
          <Text className="font-suRegular text-3xl text-slate-600">Login below or </Text>
          <Link href="/register" className="font-suBold text-slate-600 text-3xl underline">create an account</Link>
        </View>
      </View>

      <View className="w-[70%] h-fit">
        <FormField 
          label="Email"
          fieldType="Email"
          handleChangeText={(e) => setForm({...form, email: e})}
        />
        <FormField 
          label="Password"
          fieldType="Password"
          handleChangeText={(e) => setForm({...form, email: e})}
        />
      </View>
      <View className="w-[70%] h-[30vh] flex flex-col justify-end items-center">
        <CustomButton 
          text="Sign In"
          handlePress={() => {}}
        />
        <Link href="/" className="text-3xl font-suBold text-slate-600 mt-4 underline">Forgot password</Link>
      </View>
    </SafeAreaView>
  )
}

export default login