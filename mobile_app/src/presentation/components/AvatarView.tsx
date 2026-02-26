import React from 'react';
import { requireNativeComponent, ViewProps } from 'react-native';

interface AvatarViewProps extends ViewProps {
    name: string;
}

const NativeAvatarView = requireNativeComponent<AvatarViewProps>('AvatarView');

export const AvatarView: React.FC<AvatarViewProps> = (props) => {
    return <NativeAvatarView {...props} style={[{ width: 100, height: 100 }, props.style]} />;
};
