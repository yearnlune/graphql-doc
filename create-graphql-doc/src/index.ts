import prompts from 'prompts';
import path from 'path';

export default async function init(
  rootDir: string,
): Promise<void> {
  const [docPath] = await Promise.all([
    getDocPath(),
  ]);

  const dest = path.resolve(rootDir, docPath);
}

async function getDocPath(): Promise<string> {
  const {docPath} = (await prompts(
    {
      type: 'text',
      name: 'docPath',
      message: 'Where would you like to create it?',
      initial: '/doc',
    },
    {
      onCancel() {
        process.exit(1);
      },
    },
  )) as {docPath: string};
  return docPath;
}
