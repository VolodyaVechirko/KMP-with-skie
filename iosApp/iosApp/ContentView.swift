import SwiftUI
import Shared

struct ContentView: View {
    private let message = Greeting().greet()
    private let repo = SharedRepository(url: "www.cloud.ios.com")
    
    @ObservedObject
    private var chatVM = ChatViewModel()
    
    @State
    private var user: UserModel?
    
    @State
    private var address: AddressModel?
    
    var body: some View {
        VStack {
            Image(systemName: "swift")
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundColor(.accentColor)
                .frame(width: 200, height: 200)
                .padding()
            Text("SwiftUI: \(message)")
            Text("")
            
            // Wait for suspend function
            Text("User: \(user?.toPrint() ?? "loading...")")
            
            // Wait for suspend function
            Text("Address: \(address?.format() ?? "loading...")")
            
            // Print timer
            Text("Time: \(chatVM.timer) seconds")
            
            // Observe flow on UI as chat messages
            VStack(alignment: .leading, spacing: 10) {
                ForEach(chatVM.chat, id: \.self) { it in
                    Text("--: \(it)")
                }
            }.padding()
            
            Spacer()
        }
        .padding()
        .onAppear {
            chatVM.load()
            Task {
                user = try? await repo.getUser(userId: 99)
                address = try? await repo.getAddress(short: true)
            }
        }
        .onDisappear {
            chatVM.cancel()
        }
    }
}

// Build extension to Kotlin model in Swift
extension AddressModel {
    func formatSwift() -> String {
        "\(street) \(String(flat)), Swift"
    }
}

// Flow Translations
// Flow -> SkieSwiftFlow
// SharedFlow -> SkieSwiftSharedFlow
// StateFlow -> SkieSwiftStateFlow

@MainActor
class ChatViewModel: ObservableObject {
    private let repo = SharedRepository(url: "test")
    private var task: Task<Void, Never>?
    private var task2: Task<Void, Never>?
    
    @Published
    var chat: [String] = []
    
    @Published
    var timer: Int = 0
    
    func load() {
        task = Task {
            for await it in repo.messagesFlow() {
                self.chat.append(it)
            }
        }
        
        task2 = Task {
            for await it in repo.sharedFlow {
                self.timer = it.intValue
            }
        }
    }
    
    func cancel() {
        task?.cancel()
        task2?.cancel()
    }
    
    func test() {
        Task {
//            let sharedFlow: SkieSwiftSharedFlow<String>
            let flow: SkieSwiftFlow<String> = repo.messagesFlow()
            for await it in flow {
                print(it)
            }
        }
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
