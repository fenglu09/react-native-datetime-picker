import {
    NativeModules,
    Platform
} from 'react-native';
let { DatePicker } = NativeModules
DatePicker = Platform.OS == 'ios' ? {}: DatePicker;
export default DatePicker;