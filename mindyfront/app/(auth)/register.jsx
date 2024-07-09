import { View, Text, SafeAreaView, Image, TouchableOpacity } from 'react-native'
import React, { useState } from 'react'
import { Link, router } from 'expo-router'
import CustomButton from '../../components/CustomButton'
import FormField from '../../components/FormField'
import icons from '../../constants/icons'

const register = () => {
  const [form, setForm] = useState({
    email: "",
    password: ""
  })


  return (
    <SafeAreaView className="h-full w-full flex-col items-center justify-center bg-white">
      <View className="w-[80%] h-[10vh] flex flex-row justify-start items-center">
        <TouchableOpacity
          className="rounded-full h-[70px] w-[70px] flex flex-row items-center bg-gray-200 justify-center"
          href="/login"
          onPress={() => {router.back()}}
        >
          <Image 
            source={icons.arrowLeft}
            className="h-[50px] w-[50px]"
            resizeMode="cover"
          /> 
        </TouchableOpacity>
      </View>
      <View className="w-[80%] py-8 h-fit">
        <Text className="font-suBold text-6xl text-slate-600 mb-3">Create an account</Text>
        <View className="d-flex flex-row justify-start gap-1">
          <Text className="font-suRegular text-3xl text-slate-600">Enter your account details below or </Text>
          <Link href="/register" className="font-suBold text-slate-600 text-3xl underline">login here</Link>
        </View>
      </View>

      <View className="w-[80%] h-fit">
      <FormField 
          label="Username"
          fieldType="Text"
          handleChangeText={(e) => setForm({...form, email: e})}
        />
        <FormField 
          label="Date of Birth"
          fieldType="Date"
          handleChangeText={(e) => setForm({...form, email: e})}
        />
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
      <View className="w-[80%] h-[15vh] flex flex-col justify-end items-center">
        <CustomButton 
          text="Sign In"
          handlePress={() => {}}
        />
        <Link href="/" className="text-3xl font-suBold text-slate-600 mt-4 underline">Forgot password</Link>
      </View>
    
    </SafeAreaView>
  )
}

export default register