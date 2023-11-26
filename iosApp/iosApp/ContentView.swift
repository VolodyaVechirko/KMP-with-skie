import SwiftUI
import Shared

struct ContentView: View {
    private let message = Greeting().greet()
    private let repo = SharedRepository(url: "cloud.ios.com")
    
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
        .onDisappear {
            chatVM.cancel()
        }
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

    @Published
    var chat: [String] = []

    func load() {
        task = Task {
            for await it in repo.messagesFlow() {
                self.chat.append(it)
            }
        }
    }
    
    func cancel() {
         task?.cancel()
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
