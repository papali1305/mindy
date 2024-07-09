import { View, Text, TextInput, Image, TouchableOpacity } from 'react-native'
import React, { useState } from 'react'
import icons from '../constants/icons'

const FormField = ({ label, fieldType, handleChangeText }) => {
  const [showPassword, setShowPassword] = useState(true)

  const toggleShowPassword = () => {
    setShowPassword(!showPassword)
    console.log(!showPassword)
  }

  return (
    <View className="w-[100%] my-5">
      <View>
        <Text className="text-slate-600 text-3xl mb-2">{label}</Text>
      </View>
      <View className="border border-black rounded-md h-[70px] px-5 flex flex-row justify-start items-center relative">

        <TextInput
          className="w-[92%] h-full font-suRegular text-slate-600 text-2xl flex flex-row items-center"
          secureTextEntry={!showPassword && fieldType == "Password"}
          onChangeText={handleChangeText}
        />

        {fieldType == "Password" && (
          <TouchableOpacity
            onPress={toggleShowPassword}
          >
            <Image
              className="h-[45px] w-[45px]"
              source={showPassword ? icons.eye : icons.eyeHide}
              resizeMode="cover"
            />
          </TouchableOpacity>
        )}
      </View>
    </View>
  )
}

export default FormField