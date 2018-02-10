package device.Pcsc;
//===========================================================================================
// 
//  Author          : Teosseth G. Altar
// 
//  File            : Helper.java
// 
//  Copyright (C)   : Advanced Card Systems Ltd.
// 
//  Description     : Container class for helper/utility functions
// 
//  Date            : October 18, 2011
// 
//  Revision Trail : [Author] / [Date of modification] / [Details of Modifications done]
// 
// 
//=========================================================================================

public class Helper 
{
	public static String byteAsString(byte data)
	{
		String tmpStr = "";

		tmpStr += String.format("%02X", data);

		return tmpStr;
	}
	
	public static String byteAsString(byte[] data, boolean spaceinbetween)
	{
		String tmpStr = "";

		if(data == null)
			return "";

		for (int i = 0; i < data.length; i++)
			tmpStr += String.format((spaceinbetween ? "%02X " : "%02X"), data[i]);

		return tmpStr;
	}

	public static byte[] getBytes(String stringBytes, String delimeter)
	{
		String[] arrayStr = stringBytes.split(delimeter);
		byte[] bytesResult = new byte[arrayStr.length];

		for (int i = 0; i < arrayStr.length; i++)
			bytesResult[i] = Byte.parseByte(arrayStr[i]);

		return bytesResult;
	}

	public static byte[] getBytes(String stringBytes)
	{
		String formattedString = "";
		int counter = 0;

		if(stringBytes.trim() == "")
			return null;

		for(int i = 0; i < stringBytes.length(); i++)
		{
			if(stringBytes.charAt(i) == ' ')
				continue;

			if(counter > 0 && counter % 2 == 0)
				formattedString += " ";

			formattedString += stringBytes.charAt(i);

			counter++;
		}

		return getBytes(formattedString, " ");
	}

	public static byte[] stringToByteArray(String str)
	{
		byte[] buffer = new byte[str.length()/2];
		String temp;

		for (int i = 0; i < buffer.length; i++ )
		{
			temp = str.substring(i*2, i*2 + 2);
			buffer[i] = (byte)((Integer)Integer.parseInt(temp, 16)).byteValue();
		}

		return buffer; 
	}

	public static String byteArrayToString (byte[] data)
	{
		String str = "";

		for (int i = 0; i < data.length; i++)
		{
			str += (char)data[i];
		}

		return str;
	}

	public static int byteToInt(byte[] data, boolean isLittleEndian)
	{
		byte[] holder = new byte[4];
		byte[] reverseArray = new byte[4];

		if (isLittleEndian)
		{
			// Make sure that the array size is 4
			System.arraycopy(data, 0, holder, 0, data.length);

			for (int i = 0; i < 4; i++)
				reverseArray[i] = holder[3 - i];

			return byteToInt(reverseArray);
		}
		else
		{
			return byteToInt(data);
		}		
	}

	public static int byteToInt(byte[] data)
	{
		byte[] holder = new byte[4];

		if (data == null)
			return -1;

		// Make sure that the array size is 4
		System.arraycopy(data, 0, holder, 4 - data.length, data.length);

		return (((holder[0] & 0xFF) << 24) + ((holder[1] & 0xFF) << 16) + ((holder[2] & 0xFF) << 8) + (holder[3] & 0xFF)); 
	}
	
	public static byte[] intToByte(int number)
	{
		byte[] data = new byte[4];

		data[0] = (byte)((number >> 24) & 0xFF);
		data[1] = (byte)((number >> 16) & 0xFF);
		data[2] = (byte)((number >> 8) & 0xFF);
		data[3] = (byte)(number & 0xFF);

		return data;
	}

	public static String removeWhiteSpaces(String str) 
	{
		return str.replaceAll("\\s", "");		
	}
	
	// Convert Hex String to Byte Array
	public static byte[] hex2Byte(String str)
	{
		byte[] bytes = new byte[str.length() / 2];
	    for (int i = 0; i < bytes.length; i++)
	    {
	    	bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
	    }
	    return bytes;
	}
	
	public static byte[] appendArrays(byte[] arr1, byte arr2)
    {
        byte[] c = new byte[1 + arr1.length];
        System.arraycopy(arr1, 0, c, 0, arr1.length);
        c[arr1.length] = arr2;
        return c;
    }
	
	public static byte[] appendArrays(byte[] arr1, byte[] arr2)
    {
        byte[] c = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, c, 0, arr1.length);
        System.arraycopy(arr2, 0, c, arr1.length, arr2.length);
        return c;
    }
	
	public static byte[] appendArrays(byte arr1, byte[] arr2)
    {        
        byte[] c = new byte[1 + arr2.length];
        System.arraycopy(arr2, 0, c, 1, arr2.length);
        c[0] = arr1;
        return c;
    }
	
	public static Object resizeArray(Object oldArray, int newSize)
	{
	   int oldSize = java.lang.reflect.Array.getLength(oldArray);
	   @SuppressWarnings("rawtypes")
	   Class elementType = oldArray.getClass().getComponentType();
	   Object newArray = java.lang.reflect.Array.newInstance(
	         elementType, newSize);
	   int preserveLength = Math.min(oldSize, newSize);
	   if (preserveLength > 0)
	      System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
	   return newArray;
	}
}
