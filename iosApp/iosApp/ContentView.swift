import SwiftUI
import Shared

struct ContentView: View {
    private var message = Greeting().greet()
    private var repo = SharedRepository(url: "cloud.ios.com")

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
            
             let user = repo.getUser()
             Text("User: \(user.toPrint())")

             // Wait for suspend function
             let address = repo.getAddress()
             Text("Address: \(address.format())")

            let web = repo.getWebAddress()
            Text("Web: \(web.url):\(web.port)")
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
