import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                // Let Compose draw edge-to-edge; its Scaffold/TopAppBar apply the
                // status-bar and home-indicator insets, so the app bar colour fills
                // the status-bar area instead of leaving a white strip.
                .ignoresSafeArea()
    }
}



