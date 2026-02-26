import UIKit

@objc(AvatarView)
class AvatarView: UIView {
    private let label = UILabel()
    
    @objc var name: String = "" {
        didSet {
            setupUI()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.layer.masksToBounds = true
        setupLabel()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupLabel() {
        label.textColor = .white
        label.textAlignment = .center
        label.font = .boldSystemFont(ofSize: 20)
        addSubview(label)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.layer.cornerRadius = frame.width / 2
        label.frame = bounds
    }
    
    private func setupUI() {
        label.text = extractInitials(name)
        self.backgroundColor = generateColor(name)
    }
    
    private func extractInitials(_ name: String) -> String {
        let parts = name.split(separator: " ")
        if parts.count >= 2 {
            return String(parts[0].prefix(1) + parts[1].prefix(1)).uppercased()
        }
        return String(name.prefix(2)).uppercased()
    }
    
    private func generateColor(_ name: String) -> UIColor {
        let hash = abs(name.hashValue)
        let r = CGFloat((hash & 0xFF0000) >> 16) / 255.0
        let g = CGFloat((hash & 0x00FF00) >> 8) / 255.0
        let b = CGFloat(hash & 0x0000FF) / 255.0
        return UIColor(red: r, green: g, blue: b, alpha: 1.0)
    }
}
