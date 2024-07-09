import { View, Text, TouchableOpacity } from 'react-native'
import React from 'react'

const CustomButton = ({ text, handlePress }) => {
  return (
    <TouchableOpacity className="w-full h-[70px] bg-slate-600 rounded-lg flex justify-center items-center">
      <Text className="text-3xl text-white font-suRegular">{ text }</Text>
    </TouchableOpacity>
  )
}

export default CustomButton