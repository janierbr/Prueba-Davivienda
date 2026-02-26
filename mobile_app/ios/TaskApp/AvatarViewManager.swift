import Foundation
import React

@objc(AvatarViewManager)
class AvatarViewManager: RCTViewManager {
    override func view() -> UIView! {
        return AvatarView()
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
