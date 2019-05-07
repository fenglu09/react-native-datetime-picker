import {
    NativeModules,
    Platform
} from 'react-native';
const { DatePicker: AndroidDatePicker } = NativeModules

const DatePicker = Platform.OS == 'ios' ? {} : AndroidDatePicker;
export default DatePicker;