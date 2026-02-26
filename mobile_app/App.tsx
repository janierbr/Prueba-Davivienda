import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { RealmProvider } from './src/data/local/TaskSchema';
import { RootNavigator } from './src/presentation/navigation/RootNavigator';
import { GestureHandlerRootView } from 'react-native-gesture-handler';

const App = () => {
  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <RealmProvider>
        <NavigationContainer>
          <RootNavigator />
        </NavigationContainer>
      </RealmProvider>
    </GestureHandlerRootView>
  );
};

export default App;
