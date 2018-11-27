import {
    NativeModules,
    Platform
} from 'react-native';
const { DatePicker } = NativeModules
DatePicker = Platform.OS == 'ios' ? {}: DatePicker;
export default DatePicker;