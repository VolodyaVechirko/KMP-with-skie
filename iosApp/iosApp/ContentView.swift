import SwiftUI
import Shared

struct ContentView: View {
    private var message = Greeting().greet()
    private var repo = SharedRepository(url: "cloud.ios.com")
    
    @State
    private var user: UserModel?
    
    @State
    private var address: AddressModel?
    
    @StateObject                         // How to pass self.repo??
    var chatVM = ChatViewModel(repository: SharedRepository(url: "test"))
    
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
                user = try? await repo.getUser()
                address = try? await repo.getAddress()
            }
        }
    }
}

// Flow Translations
// Flow -> SkieSwiftFlow
// SharedFlow -> SkieSwiftSharedFlow
// StateFlow -> SkieSwiftStateFlow

@MainActor
class ChatViewModel: ObservableObject {
    let repo: SharedRepository

    init(repository: SharedRepository) {
        self.repo = repository
    }

    @Published
    var chat: [String] = []

    func load() {
        Task {
            for await it in repo.messagesFlow() {
                self.chat.append(it)
            }
        }
    }
    
    func test() {
        Task {
            let flow: SkieSwiftFlow<String> = repo.messagesFlow()
            for await it in flow {
                print(it)
            }
        }
    }
}

// Kotlin Enum -> Swift Enum
// Kotlin Sealed class -> Swift Enum with associated value

func testSealedClass(status: Status) {
    switch onEnum(of: status) {
    case .loading:
        print("loading...")
    case .error(let data):
        print("Error: \(data.message)")
    case .success(let data):
        print("Success: \(data.result)")
    }
}

//extension ContentView {
//    @MainActor
//    class ViewModel: ObservableObject {
//        @Published
//        var greetings: [String] = []
//
//        func startObserving() {
//            Task {
//                for await phrase in Greeting().greet() {
//                    self.greetings.append(phrase)
//                }
//            }
//        }
//    }
//}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
