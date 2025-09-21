<h1>Arquitetura do aplicativo</h1>

<p>✅ A comunicação deve ser realizada entre os clientes, por intermédio do servidor;</p>
<p>✅ Toda mensagem enviada de um cliente deve ser direcionada a um único destino, isto é, outro cliente (exemplo: Alice envia uma mensagem para Bob);</p>
<p>✅ Permitir listar todos os clientes conectados pelo comando /users;</p>
<p>✅ O servidor deve ser responsável apenas por rotear as mensagens entre os clientes;</p>
<p>❌ Os clientes devem ser capazes de enviar e receber mensagens de texto ou arquivos;</p>
<p>✅ A qualquer momento o cliente pode finalizar a comunicação ao informar o comando /sair; e</p>
<p>❌ O servidor deve manter um log em arquivo dos clientes que se conectaram, contendo os endereços IP e a data e hora de conexão.</p>

<h2>Para envio de mensagens de textos</h2>

<p>✅ O envio de mensagens deve utilizar o comando /send message <destinatario> <mensagem>;</p>
<p>✅ As mensagens devem ser exibidas pelo destinatário na saída padrão (System.out), mostrando o nome do remetente seguido da mensagem;</p>
  
<h2>Para envio de arquivos</h2>
  
<p>❌ O envio de arquivos deve utilizar o comando /send file <destinatario> <caminho do arquivo>;</p>
<p>❌ O remetente deve informar o caminho do arquivo e o programa cliente deve ler os bytes do arquivo e enviá-los via socket;</p>
<p>❌ O destinatário deve gravar os bytes recebidos com o nome original do arquivo no diretório corrente onde o programa foi executado;</p>
  
<h3>O trabalho pode ser feito em equipes de até 4 integrantes. O trabalho representa 30% da nota do semestre. O trabalho deve ser entregue pelo Moodle e prazo de entrega é 26/09.</h3>
